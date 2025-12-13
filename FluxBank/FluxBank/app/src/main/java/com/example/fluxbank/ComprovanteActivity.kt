package com.example.fluxbank

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.fluxbank.utils.TokenManager
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ComprovanteActivity : BaseActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprovante)

        tokenManager = TokenManager(this)

        val pixKey = intent.getStringExtra("PIX_KEY") ?: ""
        val valorString = intent.getStringExtra("VALOR") ?: "0"
        val transacaoId = intent.getLongExtra("TRANSACAO_ID", 0L)
        val nomeDestinatario = intent.getStringExtra("NOME_DESTINATARIO") ?: "Destinatário"
        val documentoMascarado = intent.getStringExtra("DOCUMENTO_MASCARADO") ?: "***.***.***: ***-**"
        val tipoDocumento = intent.getStringExtra("TIPO_DOCUMENTO") ?: "CPF"
        val instituicao = intent.getStringExtra("INSTITUICAO") ?: "FluxBank"

        Log.d("Comprovante", "========== DADOS RECEBIDOS ==========")
        Log.d("Comprovante", "Nome Destinatário: $nomeDestinatario")
        Log.d("Comprovante", "Documento: $documentoMascarado")
        Log.d("Comprovante", "Tipo: $tipoDocumento")
        Log.d("Comprovante", "Instituição: $instituicao")
        Log.d("Comprovante", "Chave: $pixKey")
        Log.d("Comprovante", "=====================================")

        val valorFormatado = try {
            val valorDouble = valorString.toDouble()
            NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valorDouble)
        } catch (e: NumberFormatException) {
            "R$ 0,00"
        }

        val sdfData = SimpleDateFormat("EEEE, dd/MM/yyyy", Locale("pt", "BR"))
        val sdfHora = SimpleDateFormat("HH'h'mm", Locale("pt", "BR"))
        val dataAtual = Date()

        val idTransacao = "E${transacaoId.toString().padStart(10, '0')}"

        findViewById<TextView>(R.id.payment_value).text = valorFormatado

        setDetailRow(R.id.data_pagamento, "Data pagamento:", sdfData.format(dataAtual).replaceFirstChar { it.uppercase() })
        setDetailRow(R.id.horario, "Horário:", sdfHora.format(dataAtual))
        setDetailRow(R.id.id_transacao, "ID da transação:", idTransacao)

        Log.d("Comprovante", "Definindo dados do DESTINATÁRIO...")
        setDetailRow(R.id.nome_recebeu, "Nome:", nomeDestinatario)
        setDetailRow(R.id.cpf_recebeu, "$tipoDocumento:", documentoMascarado)
        setDetailRow(R.id.instituicao_recebeu, "Instituição:", instituicao)
        setDetailRow(R.id.chave_recebeu, "Chave:", pixKey)

        carregarDadosUsuario()

        val btnCompartilhar = findViewById<Button>(R.id.btn_compartilhar)
        btnCompartilhar.setOnClickListener {
            compartilharComprovante()
        }

        val btnNovoPix = findViewById<Button>(R.id.btn_novo_pix)
        val btnVoltarInicio = findViewById<Button>(R.id.btn_voltar_inicio)

        btnNovoPix.setOnClickListener {
            val intent = Intent(this, PixActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        btnVoltarInicio.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun carregarDadosUsuario() {
        lifecycleScope.launch {
            try {
                val userName = tokenManager.getUserName() ?: "Usuário"
                val userCpf = tokenManager.getUserCpf() ?: ""
                val userCnpj = tokenManager.getUserCnpj()

                val documentoMascarado = if (userCpf.isNotEmpty()) {
                    if (userCpf.length == 11) {
                        "***.${userCpf.substring(3, 6)}.${userCpf.substring(6, 9)}-**"
                    } else {
                        "***.***.***: ***-**"
                    }
                } else if (userCnpj != null && userCnpj.isNotEmpty()) {
                    if (userCnpj.length == 14) {
                        "**.${userCnpj.substring(2, 5)}.${userCnpj.substring(5, 8)}/****-**"
                    } else {
                        "**.***.****: /****: /**-**"
                    }
                } else {
                    "***.***.***: ***-**"
                }

                setDetailRow(R.id.nome_pagou, "Nome:", userName)
                setDetailRow(R.id.cpf_pagou, if (userCnpj != null && userCnpj.isNotEmpty()) "CNPJ:" else "CPF:", documentoMascarado)
                setDetailRow(R.id.instituicao_pagou, "Instituição:", "FluxBank")

            } catch (e: Exception) {
                Log.e("Comprovante", "Erro ao carregar dados", e)

                setDetailRow(R.id.nome_pagou, "Nome:", "Usuário")
                setDetailRow(R.id.cpf_pagou, "CPF:", "***.***.***: ***-**")
                setDetailRow(R.id.instituicao_pagou, "Instituição:", "FluxBank")
            }
        }
    }

    private fun setDetailRow(@IdRes viewId: Int, labelText: String, valueText: String?) {
        try {
            val rowView = findViewById<View>(viewId)

            if (rowView == null) {
                Log.e("Comprovante", "View $viewId não encontrada!")
                return
            }

            val labelView = rowView.findViewById<TextView>(R.id.label)
            val valueView = rowView.findViewById<TextView>(R.id.value)

            if (labelView != null && valueView != null) {
                labelView.text = labelText
                valueView.text = valueText
                Log.d("Comprovante", "$labelText = $valueText")
            } else {
                Log.e("Comprovante", "label/value não encontrado em $viewId")
            }

        } catch (e: Exception) {
            Log.e("Comprovante", "Erro ao definir $viewId: ${e.message}")
        }
    }

    private fun compartilharComprovante() {
        try {
            // Capturar screenshot do comprovante
            val view = window.decorView.rootView
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)

            val cachePath = File(cacheDir, "images")
            cachePath.mkdirs()
            val file = File(cachePath, "comprovante_${System.currentTimeMillis()}.png")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()

            val contentUri: Uri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_TEXT, "Comprovante de Pix - FluxBank")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(Intent.createChooser(shareIntent, "Compartilhar comprovante via"))

        } catch (e: Exception) {
            Log.e("Comprovante", "Erro ao compartilhar", e)
            Toast.makeText(this, "Erro ao compartilhar comprovante", Toast.LENGTH_SHORT).show()
        }
    }
}