package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.fluxbank.network.ApiClient
import com.example.fluxbank.network.models.PixRequest
import com.example.fluxbank.utils.TokenManager
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class ConfirmarPagamentoActivity : BaseActivity() {

    private lateinit var tokenManager: TokenManager
    private var transacaoId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmar_pagamento)

        tokenManager = TokenManager(this)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val paymentValue = findViewById<TextView>(R.id.payment_value)
        val recipientName = findViewById<TextView>(R.id.recipient_name)
        val recipientCpf = findViewById<TextView>(R.id.recipient_cpf)
        val recipientInstitution = findViewById<TextView>(R.id.recipient_institution)
        val recipientKey = findViewById<TextView>(R.id.recipient_key)
        val btnPagar = findViewById<Button>(R.id.btnPagar)

        val pixKey = intent.getStringExtra("PIX_KEY") ?: ""
        val valorString = intent.getStringExtra("VALOR") ?: "0"

        // Formata o valor
        val valorFormatado = try {
            val valorDouble = valorString.toDouble()
            NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valorDouble)
        } catch (e: NumberFormatException) {
            "R$ 0,00"
        }

        paymentValue.text = valorFormatado
        recipientKey.text = pixKey
        btnPagar.text = "Pagar $valorFormatado"

        // TODO: Buscar dados do destinatário pela chave PIX
        recipientName.text = "Carregando..."
        recipientCpf.text = "***.***.***: ***-**"
        recipientInstitution.text = "Verificando..."

        btnBack.setOnClickListener {
            finish()
        }

        btnPagar.setOnClickListener {
            btnPagar.isEnabled = false
            btnPagar.text = "Processando..."
            iniciarPix(pixKey, valorString)
        }
    }

    private fun iniciarPix(chavePix: String, valorString: String) {
        lifecycleScope.launch {
            try {
                Log.d("ConfirmarPagamento", "=== INICIANDO PIX ===")

                val token = tokenManager.getToken()
                val contaId = tokenManager.getContaId()

                if (token == null || contaId == 0L) {
                    Log.e("ConfirmarPagamento", "Token ou ContaId inválido")
                    Toast.makeText(
                        this@ConfirmarPagamentoActivity,
                        "Erro: Sessão inválida",
                        Toast.LENGTH_LONG
                    ).show()
                    findViewById<Button>(R.id.btnPagar).isEnabled = true
                    return@launch
                }

                val valor = valorString.toDouble()

                val request = PixRequest(
                    contaOrigemId = contaId,
                    chavePix = chavePix,
                    valor = valor,
                    descricao = "Transferência Pix",
                    requerVerificacao = true
                )

                Log.d("ConfirmarPagamento", "Request: $request")
                Log.d("ConfirmarPagamento", "Token: ${token.substring(0, 20)}...")

                val response = ApiClient.api.iniciarPix(request, "Bearer $token")

                Log.d("ConfirmarPagamento", "Status: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    val transacao = response.body()!!

                    Log.d("ConfirmarPagamento", "✅ Transação iniciada!")
                    Log.d("ConfirmarPagamento", "ID: ${transacao.id}")
                    Log.d("ConfirmarPagamento", "Status: ${transacao.statusTransacao}")
                    Log.d("ConfirmarPagamento", "Requer verificação: ${transacao.requerVerificacao}")

                    transacaoId = transacao.id

                    if (transacao.requerVerificacao == true) {
                        val intent = Intent(this@ConfirmarPagamentoActivity, SenhaPagamentoActivity::class.java)
                        intent.putExtra("PIX_KEY", chavePix)
                        intent.putExtra("VALOR", valorString)
                        intent.putExtra("TRANSACAO_ID", transacao.id)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@ConfirmarPagamentoActivity, PagamentoSucessoActivity::class.java)
                        intent.putExtra("PIX_KEY", chavePix)
                        intent.putExtra("VALOR", valorString)
                        intent.putExtra("TRANSACAO_ID", transacao.id)
                        startActivity(intent)
                        finish()
                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ConfirmarPagamento", "❌ Erro: $errorBody")

                    Toast.makeText(
                        this@ConfirmarPagamentoActivity,
                        "Erro ao iniciar PIX: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()

                    findViewById<Button>(R.id.btnPagar).apply {
                        isEnabled = true
                        text = "Pagar novamente"
                    }
                }

            } catch (e: Exception) {
                Log.e("ConfirmarPagamento", "Exceção", e)

                Toast.makeText(
                    this@ConfirmarPagamentoActivity,
                    "Erro: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

                findViewById<Button>(R.id.btnPagar).apply {
                    isEnabled = true
                    text = "Pagar novamente"
                }
            }
        }
    }
}