package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.fluxbank.network.ApiClient
import com.example.fluxbank.network.models.VerificarTransacaoRequest
import com.example.fluxbank.utils.TokenManager
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class SenhaPagamentoActivity : BaseActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senha_pagamento)

        tokenManager = TokenManager(this)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val txtValor = findViewById<TextView>(R.id.txtValor)
        val txtDestinatario = findViewById<TextView>(R.id.txtDestinatario)
        val edtSenha = findViewById<EditText>(R.id.edtSenhaTransacao)
        val btnConfirmar = findViewById<ImageButton>(R.id.btnConfirmar)

        val pixKey = intent.getStringExtra("PIX_KEY") ?: ""
        val valorString = intent.getStringExtra("VALOR") ?: "0"
        val transacaoId = intent.getLongExtra("TRANSACAO_ID", 0L)
        val nomeDestinatario = intent.getStringExtra("NOME_DESTINATARIO") ?: "Destinatário"
        val documentoMascarado = intent.getStringExtra("DOCUMENTO_MASCARADO") ?: ""
        val tipoDocumento = intent.getStringExtra("TIPO_DOCUMENTO") ?: "CPF"
        val instituicao = intent.getStringExtra("INSTITUICAO") ?: "FluxBank"

        val valorFormatado = try {
            val valorDouble = valorString.toDouble()
            NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valorDouble)
        } catch (e: NumberFormatException) {
            "R$ 0,00"
        }

        txtValor.text = valorFormatado
        txtDestinatario.text = "Para: $nomeDestinatario"

        edtSenha.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length > 6) {
                    edtSenha.setText(s.substring(0, 6))
                    edtSenha.setSelection(6)
                }
            }
        })

        btnBack.setOnClickListener {
            finish()
        }

        btnConfirmar.setOnClickListener {
            val senhaTransacao = edtSenha.text.toString()

            if (senhaTransacao.length != 6) {
                edtSenha.error = "A senha deve ter 6 dígitos"
                return@setOnClickListener
            }

            btnConfirmar.isEnabled = false

            verificarESenhaPagar(
                transacaoId,
                senhaTransacao,
                pixKey,
                valorString,
                nomeDestinatario,
                documentoMascarado,
                tipoDocumento,
                instituicao,
                btnConfirmar
            )
        }
    }

    private fun verificarESenhaPagar(
        transacaoId: Long,
        senhaTransacao: String,
        pixKey: String,
        valorString: String,
        nomeDestinatario: String,
        documentoMascarado: String,
        tipoDocumento: String,
        instituicao: String,
        btnConfirmar: ImageButton
    ) {
        lifecycleScope.launch {
            try {
                Log.d("SenhaPagamento", "=== VERIFICANDO SENHA DE TRANSAÇÃO ===")

                val token = tokenManager.getToken()

                if (token == null) {
                    Toast.makeText(
                        this@SenhaPagamentoActivity,
                        "Erro: Sessão inválida",
                        Toast.LENGTH_LONG
                    ).show()
                    btnConfirmar.isEnabled = true
                    return@launch
                }

                val request = VerificarTransacaoRequest(
                    transacaoId = transacaoId,
                    codigoVerificacao = senhaTransacao
                )

                Log.d("SenhaPagamento", "TransacaoID: $transacaoId")

                val response = ApiClient.api.verificarPix(request, "Bearer $token")

                Log.d("SenhaPagamento", "Status: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    val transacao = response.body()!!

                    Log.d("SenhaPagamento", "Senha correta! Transação concluída!")
                    Log.d("SenhaPagamento", "Status: ${transacao.statusTransacao}")

                    val intent = Intent(this@SenhaPagamentoActivity, PagamentoSucessoActivity::class.java)
                    intent.putExtra("PIX_KEY", pixKey)
                    intent.putExtra("VALOR", valorString)
                    intent.putExtra("TRANSACAO_ID", transacaoId)
                    intent.putExtra("NOME_DESTINATARIO", nomeDestinatario)
                    intent.putExtra("DOCUMENTO_MASCARADO", documentoMascarado)
                    intent.putExtra("TIPO_DOCUMENTO", tipoDocumento)
                    intent.putExtra("INSTITUICAO", instituicao)
                    startActivity(intent)
                    finish()

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("SenhaPagamento", "Erro: $errorBody")

                    val errorMessage = when {
                        errorBody?.contains("incorreta") == true -> "Senha de transação incorreta"
                        errorBody?.contains("Saldo insuficiente") == true -> "Saldo insuficiente"
                        errorBody?.contains("já foi processada") == true -> "Transação já foi processada"
                        else -> "Erro ao processar pagamento"
                    }

                    Toast.makeText(
                        this@SenhaPagamentoActivity,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()

                    findViewById<EditText>(R.id.edtSenhaTransacao).setText("")

                    btnConfirmar.isEnabled = true
                }

            } catch (e: Exception) {
                Log.e("SenhaPagamento", "Exceção", e)

                Toast.makeText(
                    this@SenhaPagamentoActivity,
                    "Erro: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

                btnConfirmar.isEnabled = true
            }
        }
    }
}