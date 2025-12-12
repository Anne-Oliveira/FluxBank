package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.fluxbank.network.ApiClient
import com.example.fluxbank.network.models.VerificarTransacaoRequest
import com.example.fluxbank.utils.TokenManager
import kotlinx.coroutines.launch

class SenhaPagamentoActivity : BaseActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senha_pagamento)

        tokenManager = TokenManager(this)

        val btnNext = findViewById<ImageButton>(R.id.btnNext)
        val edtCodigo = findViewById<EditText>(R.id.edtSenha) // Reutiliza campo de senha

        val pixKey = intent.getStringExtra("PIX_KEY") ?: ""
        val valor = intent.getStringExtra("VALOR") ?: "0"
        val transacaoId = intent.getLongExtra("TRANSACAO_ID", 0L)

        Log.d("SenhaPagamento", "TransacaoId: $transacaoId")

        btnNext.setOnClickListener {
            val codigo = edtCodigo.text.toString()

            if (codigo.length != 6) {
                edtCodigo.error = "Digite o código de 6 dígitos"
                return@setOnClickListener
            }

            btnNext.isEnabled = false
            verificarCodigo(transacaoId, codigo, pixKey, valor)
        }
    }

    private fun verificarCodigo(
        transacaoId: Long,
        codigo: String,
        pixKey: String,
        valor: String
    ) {
        lifecycleScope.launch {
            try {
                Log.d("SenhaPagamento", "=== VERIFICANDO CÓDIGO ===")

                val token = tokenManager.getToken()

                if (token == null) {
                    Toast.makeText(
                        this@SenhaPagamentoActivity,
                        "Erro: Token não encontrado",
                        Toast.LENGTH_LONG
                    ).show()
                    findViewById<ImageButton>(R.id.btnNext).isEnabled = true
                    return@launch
                }

                val request = VerificarTransacaoRequest(
                    transacaoId = transacaoId,
                    codigoVerificacao = codigo
                )

                Log.d("SenhaPagamento", "Request: $request")

                val response = ApiClient.api.verificarPix(request, "Bearer $token")

                Log.d("SenhaPagamento", "Status: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    val transacao = response.body()!!

                    Log.d("SenhaPagamento", "Código verificado!")
                    Log.d("SenhaPagamento", "Status: ${transacao.statusTransacao}")

                    if (transacao.statusTransacao == "CONCLUIDA") {
                        val intent = Intent(this@SenhaPagamentoActivity, PagamentoSucessoActivity::class.java)
                        intent.putExtra("PIX_KEY", pixKey)
                        intent.putExtra("VALOR", valor)
                        intent.putExtra("TRANSACAO_ID", transacaoId)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@SenhaPagamentoActivity,
                            "Status: ${transacao.statusTransacao}",
                            Toast.LENGTH_LONG
                        ).show()
                        findViewById<ImageButton>(R.id.btnNext).isEnabled = true
                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("SenhaPagamento", "Erro: $errorBody")

                    val errorMsg = when {
                        errorBody?.contains("código inválido", ignoreCase = true) == true ->
                            "Código inválido. Tente novamente."
                        errorBody?.contains("expirado", ignoreCase = true) == true ->
                            "Código expirado. Inicie novamente."
                        else ->
                            "Erro: ${response.code()}"
                    }

                    Toast.makeText(
                        this@SenhaPagamentoActivity,
                        errorMsg,
                        Toast.LENGTH_LONG
                    ).show()

                    findViewById<ImageButton>(R.id.btnNext).isEnabled = true
                }

            } catch (e: Exception) {
                Log.e("SenhaPagamento", "❌ Exceção", e)

                Toast.makeText(
                    this@SenhaPagamentoActivity,
                    "Erro: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

                findViewById<ImageButton>(R.id.btnNext).isEnabled = true
            }
        }
    }
}