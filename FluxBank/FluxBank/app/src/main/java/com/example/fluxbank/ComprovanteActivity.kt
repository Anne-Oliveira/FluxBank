package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ComprovanteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprovante)

        // Recebe os dados da tela anterior
        val pixKey = intent.getStringExtra("PIX_KEY")
        val valorString = intent.getStringExtra("VALOR")

        // Formata o valor como moeda
        val valorFormatado = try {
            val valorDouble = valorString?.toDouble() ?: 0.0
            NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valorDouble)
        } catch (e: NumberFormatException) {
            "R$ 0,00"
        }

        // Gera dados da transação
        val sdfData = SimpleDateFormat("EEEE, dd/MM/yyyy", Locale("pt", "BR"))
        val sdfHora = SimpleDateFormat("HH'h'mm", Locale("pt", "BR"))
        val dataAtual = Date()
        val idTransacao = "E" + UUID.randomUUID().toString().replace("-", "").uppercase()

        // --- Preenche os dados na tela de forma segura ---
        findViewById<TextView>(R.id.payment_value).text = valorFormatado

        // Sobre a Transação
        setDetailRow(R.id.data_pagamento, "Data pagamento:", sdfData.format(dataAtual).replaceFirstChar { it.uppercase() })
        setDetailRow(R.id.horario, "Horário:", sdfHora.format(dataAtual))
        setDetailRow(R.id.id_transacao, "ID da transação: \n \n", idTransacao)

        // Quem Recebeu
        setDetailRow(R.id.nome_recebeu, "Nome:", "Anee Oliveira")
        setDetailRow(R.id.cpf_recebeu, "CPF:", "***.111.222-**")
        setDetailRow(R.id.instituicao_recebeu, "Instituição:", "Banco Bradesco")
        setDetailRow(R.id.chave_recebeu, "Chave:", pixKey)

        // Quem Pagou
        setDetailRow(R.id.nome_pagou, "Nome:", "Seu Nome")
        setDetailRow(R.id.cpf_pagou, "CPF:", "***.333.444-**")
        setDetailRow(R.id.instituicao_pagou, "Instituição:", "FluxBank")


        // Botões
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

    private fun setDetailRow(@IdRes viewId: Int, labelText: String, valueText: String?) {
        val rowView = findViewById<View>(viewId)
        rowView?.findViewById<TextView>(R.id.label)?.text = labelText
        rowView?.findViewById<TextView>(R.id.value)?.text = valueText
    }
}
