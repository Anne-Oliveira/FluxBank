package com.example.fluxbank

import android.Manifest
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fluxbank.databinding.ActivityLeitorQrBinding
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LeitorQrActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeitorQrBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private var camera: Camera? = null
    private var isFlashlightOn = false
    private var lastScannedCode = ""
    private var lastScanTime = 0L
    private val SCAN_COOLDOWN = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeitorQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        binding.closeButton.setOnClickListener {
            finish()
        }

        binding.flashlightButton.setOnClickListener {
            toggleFlashlight()
        }

        binding.pixCopiaEColaButton.setOnClickListener {
            showPixCopiaCola()
        }
    }

    private fun startCamera() {
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QRCodeAnalyzer { qrCode ->
                        handleQRCode(qrCode)
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )
            } catch (exc: Exception) {
                Toast.makeText(this, "Erro ao iniciar câmera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun handleQRCode(qrCode: String) {
        val currentTime = System.currentTimeMillis()
        if (qrCode == lastScannedCode && (currentTime - lastScanTime) < SCAN_COOLDOWN) {
            return
        }

        lastScannedCode = qrCode
        lastScanTime = currentTime

        runOnUiThread {
            if (isValidPixQRCode(qrCode)) {
                processPixQRCode(qrCode)
            } else {
                Toast.makeText(
                    this,
                    "QR Code inválido. Por favor, escaneie um QR Code PIX válido.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isValidPixQRCode(qrCode: String): Boolean {
        return qrCode.startsWith("00020126") ||
                qrCode.startsWith("00020101") ||
                qrCode.contains("br.gov.bcb.pix")
    }

    private fun processPixQRCode(qrCode: String) {
        val pixData = parsePixQRCode(qrCode)

        AlertDialog.Builder(this)
            .setTitle("QR Code PIX Detectado")
            .setMessage(buildPixMessage(pixData))
            .setPositiveButton("Continuar") { _, _ ->
                navigateToTransfer(pixData)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun parsePixQRCode(qrCode: String): PixData {
        val pixData = PixData()

        try {
            val merchantInfo = extractTag(qrCode, "26")
            if (merchantInfo.isNotEmpty()) {
                pixData.chave = extractTag(merchantInfo, "01") // Chave PIX
            }

            pixData.merchantCategoryCode = extractTag(qrCode, "52")

            pixData.currency = extractTag(qrCode, "53")

            val amount = extractTag(qrCode, "54")
            if (amount.isNotEmpty()) {
                pixData.valor = amount.toDoubleOrNull()
            }

            pixData.nomeDestinatario = extractTag(qrCode, "59")

            pixData.cidade = extractTag(qrCode, "60")

            if (pixData.chave.isEmpty()) {
                pixData.chave = qrCode
            }

        } catch (e: Exception) {
            pixData.chave = qrCode
        }

        return pixData
    }

    private fun extractTag(data: String, tag: String): String {
        try {
            val tagIndex = data.indexOf(tag)
            if (tagIndex == -1) return ""

            val lengthStart = tagIndex + tag.length
            if (lengthStart + 2 > data.length) return ""

            val length = data.substring(lengthStart, lengthStart + 2).toIntOrNull() ?: return ""
            val valueStart = lengthStart + 2

            if (valueStart + length > data.length) return ""

            return data.substring(valueStart, valueStart + length)
        } catch (e: Exception) {
            return ""
        }
    }

    private fun buildPixMessage(pixData: PixData): String {
        val message = StringBuilder()

        if (pixData.nomeDestinatario.isNotEmpty()) {
            message.append("Destinatário: ${pixData.nomeDestinatario}\n")
        }

        if (pixData.valor != null) {
            message.append("Valor: R$ ${"%.2f".format(pixData.valor)}\n")
        } else {
            message.append("Valor: A definir\n")
        }

        if (pixData.cidade.isNotEmpty()) {
            message.append("Cidade: ${pixData.cidade}\n")
        }

        if (pixData.chave.isNotEmpty() && pixData.chave.length < 50) {
            message.append("Chave: ${pixData.chave}\n")
        }

        return message.toString().ifEmpty { "Dados do PIX carregados com sucesso!" }
    }

    private fun navigateToTransfer(pixData: PixData) {
        if (pixData.valor != null && pixData.valor!! > 0) {
            navigateToConfirmacao(pixData)
        } else {
            navigateToInserirValor(pixData)
        }
    }

    private fun navigateToConfirmacao(pixData: PixData) {
        val intent = Intent(this, ConfirmarPagamentoActivity::class.java).apply {
            putExtra("PIX_KEY", pixData.chave)
            putExtra("VALOR", pixData.valor.toString())
            putExtra("NOME_DESTINATARIO", pixData.nomeDestinatario)
            putExtra("DOCUMENTO_MASCARADO", "***.***.***-**") // Mascarado por padrão
            putExtra("TIPO_DOCUMENTO", "CPF") // Assumindo CPF por padrão
            putExtra("INSTITUICAO", pixData.cidade.ifEmpty { "Instituição Financeira" })
            putExtra("ORIGEM", "QR_CODE") // Flag para identificar origem
        }
        startActivity(intent)
        finish()
    }

    private fun navigateToInserirValor(pixData: PixData) {
        val intent = Intent(this, TransferenciaNovamenteActivity::class.java).apply {
            putExtra("TIPO_TRANSFERENCIA", "QR_CODE")
            putExtra("PIX_KEY", pixData.chave)
            putExtra("NOME_DESTINATARIO", pixData.nomeDestinatario)
            putExtra("CIDADE", pixData.cidade)
            putExtra("QR_CODE_COMPLETO", pixData.chave)
        }
        startActivity(intent)
        finish()
    }

    private fun showPixCopiaCola() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = clipboard.primaryClip

        if (clipData != null && clipData.itemCount > 0) {
            val clipText = clipData.getItemAt(0).text.toString()

            if (isValidPixQRCode(clipText)) {
                handleQRCode(clipText)
            } else {
                showPixCopiaColeDialog()
            }
        } else {
            showPixCopiaColeDialog()
        }
    }

    private fun showPixCopiaColeDialog() {
        val input = android.widget.EditText(this)
        input.hint = "Cole o código PIX aqui"

        AlertDialog.Builder(this)
            .setTitle("PIX Copia e Cola")
            .setMessage("Cole o código PIX que você copiou:")
            .setView(input)
            .setPositiveButton("Confirmar") { _, _ ->
                val pixCode = input.text.toString().trim()
                if (pixCode.isNotEmpty()) {
                    handleQRCode(pixCode)
                } else {
                    Toast.makeText(this, "Código PIX vazio", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissão de câmera negada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun toggleFlashlight() {
        camera?.cameraControl?.enableTorch(!isFlashlightOn)
        isFlashlightOn = !isFlashlightOn
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}

data class PixData(
    var chave: String = "",
    var nomeDestinatario: String = "",
    var valor: Double? = null,
    var cidade: String = "",
    var merchantCategoryCode: String = "",
    var currency: String = "",
    var qrCodeCompleto: String = ""
)

private class QRCodeAnalyzer(
    private val onQRCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        when (barcode.valueType) {
                            Barcode.TYPE_TEXT,
                            Barcode.TYPE_URL -> {
                                barcode.rawValue?.let { qrCode ->
                                    onQRCodeScanned(qrCode)
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener {
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}