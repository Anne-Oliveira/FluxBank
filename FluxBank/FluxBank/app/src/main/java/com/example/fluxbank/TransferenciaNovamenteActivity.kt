package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class TransferenciaNovamenteActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var recipientName: TextView
    private lateinit var recipientKey: TextView
    private lateinit var recipientCity: TextView
    private lateinit var valueInput: EditText
    private lateinit var btnContinue: Button

    private var pixKey: String = ""
    private var nomeDestinatario: String = ""
    private var cidade: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transferencia_novamente)

        btnBack = findViewById(R.id.btnBack)
        recipientName = findViewById(R.id.recipient_name)
        recipientKey = findViewById(R.id.recipient_key)
        recipientCity = findViewById(R.id.recipient_city)
        valueInput = findViewById(R.id.value_input)
        btnContinue = findViewById(R.id.btnContinuarNv)

        val tipoTransferencia = intent.getStringExtra("TIPO_TRANSFERENCIA")

        if (tipoTransferencia == "QR_CODE") {
            setupQRCodeTransfer()
        } else {
            setupContatoTransfer()
        }

        setupListeners()
    }

    private fun setupQRCodeTransfer() {
        pixKey = intent.getStringExtra("PIX_KEY") ?: ""
        nomeDestinatario = intent.getStringExtra("NOME_DESTINATARIO") ?: "Destinatário"
        cidade = intent.getStringExtra("CIDADE") ?: ""

        recipientName.text = nomeDestinatario
        recipientKey.text = formatarChavePix(pixKey)

        if (cidade.isNotEmpty()) {
            recipientCity.text = cidade
            recipientCity.visibility = android.view.View.VISIBLE
        } else {
            recipientCity.visibility = android.view.View.GONE
        }
    }

    private fun setupContatoTransfer() {
        pixKey = intent.getStringExtra("PIX_KEY") ?: ""
        nomeDestinatario = intent.getStringExtra("NOME_DESTINATARIO") ?: "Destinatário"

        recipientName.text = nomeDestinatario
        recipientKey.text = formatarChavePix(pixKey)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        valueInput.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    valueInput.removeTextChangedListener(this)

                    val cleanString = s.toString().replace(Regex("[R$,. ]"), "")

                    if (cleanString.isNotEmpty()) {
                        val parsed = cleanString.toDoubleOrNull() ?: 0.0
                        val formatted = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))
                            .format(parsed / 100)

                        current = formatted
                        valueInput.setText(formatted)
                        valueInput.setSelection(formatted.length)
                    }

                    valueInput.addTextChangedListener(this)
                }

                val valor = getValorFromInput()
                btnContinue.isEnabled = valor > 0
            }
        })

        btnContinue.setOnClickListener {
            val valor = getValorFromInput()

            if (valor <= 0) {
                Toast.makeText(this, "Informe um valor válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            navegarParaConfirmacao(valor)
        }
    }

    private fun getValorFromInput(): Double {
        val cleanString = valueInput.text.toString()
            .replace("R$", "")
            .replace(".", "")
            .replace(",", ".")
            .trim()

        return cleanString.toDoubleOrNull() ?: 0.0
    }

    private fun navegarParaConfirmacao(valor: Double) {
        val intent = Intent(this, ConfirmarPagamentoActivity::class.java).apply {
            putExtra("PIX_KEY", pixKey)
            putExtra("VALOR", valor.toString())
            putExtra("NOME_DESTINATARIO", nomeDestinatario)
            putExtra("DOCUMENTO_MASCARADO", "***.***.***-**")
            putExtra("TIPO_DOCUMENTO", "CPF")
            putExtra("INSTITUICAO", if (cidade.isNotEmpty()) cidade else "Instituição Financeira")
            putExtra("ORIGEM", "QR_CODE")
        }
        startActivity(intent)
        finish()
    }

    private fun formatarChavePix(chave: String): String {
        return when {
            chave.replace(Regex("[^0-9]"), "").length == 11 -> {
                val limpo = chave.replace(Regex("[^0-9]"), "")
                "${limpo.substring(0, 3)}.${limpo.substring(3, 6)}.${limpo.substring(6, 9)}-${limpo.substring(9, 11)}"
            }
            chave.replace(Regex("[^0-9]"), "").length == 14 -> {
                val limpo = chave.replace(Regex("[^0-9]"), "")
                "${limpo.substring(0, 2)}.${limpo.substring(2, 5)}.${limpo.substring(5, 8)}/${limpo.substring(8, 12)}-${limpo.substring(12, 14)}"
            }
            chave.startsWith("+55") -> {
                val limpo = chave.replace(Regex("[^0-9]"), "")
                "+${limpo.substring(0, 2)} (${limpo.substring(2, 4)}) ${limpo.substring(4, 9)}-${limpo.substring(9)}"
            }
            else -> if (chave.length > 40) "${chave.substring(0, 37)}..." else chave
        }
    }
}