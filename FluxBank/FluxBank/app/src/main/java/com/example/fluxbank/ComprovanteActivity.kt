package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.lifecycle.lifecycleScope
import com.example.fluxbank.utils.TokenManager
import kotlinx.coroutines.launch
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

        Log.d("Comprovante", "PIX: $pixKey | Valor: $valorString | ID: $transacaoId")

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
        setDetailRow(R.id.id_transacao, "ID da transação: \n \n", idTransacao)

        setDetailRow(R.id.nome_recebeu, "Nome:", "Destinatário")
        setDetailRow(R.id.cpf_recebeu, "CPF:", "***.***.***: ***-**")
        setDetailRow(R.id.instituicao_recebeu, "Instituição:", "Verificando...")
        setDetailRow(R.id.chave_recebeu, "Chave:", pixKey)

        carregarDadosUsuario()

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

                Log.d("Comprovante", "Nome: $userName")
                Log.d("Comprovante", "CPF: $userCpf")

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
                setDetailRow(R.id.cpf_pagou, if (userCnpj != null) "CNPJ:" else "CPF:", documentoMascarado)
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
        val rowView = findViewById<View>(viewId)
        rowView?.findViewById<TextView>(R.id.label)?.text = labelText
        rowView?.findViewById<TextView>(R.id.value)?.text = valueText
    }
}