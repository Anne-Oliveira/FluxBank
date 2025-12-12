package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import java.text.NumberFormat
import java.util.Locale

class DefinirValorPixActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_definir_valor_pix)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnContinuar = findViewById<Button>(R.id.btnContinuar)
        val edtValor = findViewById<EditText>(R.id.edtValor)

        val pixKey = intent.getStringExtra("PIX_KEY") ?: ""
        val contaId = intent.getLongExtra("CONTA_ID", 0L)
        val nomeDestinatario = intent.getStringExtra("NOME_DESTINATARIO") ?: "Destinatário"
        val documentoDestinatario = intent.getStringExtra("DOCUMENTO_DESTINATARIO") ?: ""
        val documentoMascarado = intent.getStringExtra("DOCUMENTO_MASCARADO") ?: ""
        val tipoDocumento = intent.getStringExtra("TIPO_DOCUMENTO") ?: "CPF"
        val instituicao = intent.getStringExtra("INSTITUICAO") ?: "FluxBank"

        findViewById<TextView>(R.id.txtDestinatario)?.text = "Transferindo para: $nomeDestinatario"

        edtValor.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return

                isUpdating = true

                val text = s.toString().replace("[^\\d]".toRegex(), "")

                if (text.isNotEmpty()) {
                    val value = text.toDouble() / 100
                    val formatted = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
                    edtValor.setText(formatted)
                    edtValor.setSelection(formatted.length)
                }

                isUpdating = false
            }
        })

        btnBack.setOnClickListener {
            finish()
        }

        btnContinuar.setOnClickListener {
            val valorTexto = edtValor.text.toString()
                .replace("R$", "")
                .replace(".", "")
                .replace(",", ".")
                .trim()

            if (valorTexto.isEmpty() || valorTexto.toDoubleOrNull() == null || valorTexto.toDouble() <= 0) {
                edtValor.error = "Digite um valor válido"
                return@setOnClickListener
            }

            val intent = Intent(this, ConfirmarPagamentoActivity::class.java)
            intent.putExtra("PIX_KEY", pixKey)
            intent.putExtra("CONTA_ID", contaId)
            intent.putExtra("VALOR", valorTexto)
            intent.putExtra("NOME_DESTINATARIO", nomeDestinatario)
            intent.putExtra("DOCUMENTO_DESTINATARIO", documentoDestinatario)
            intent.putExtra("DOCUMENTO_MASCARADO", documentoMascarado)
            intent.putExtra("TIPO_DOCUMENTO", tipoDocumento)
            intent.putExtra("INSTITUICAO", instituicao)
            startActivity(intent)
        }
    }
}