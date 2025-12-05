package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class ConfirmarPagamentoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmar_pagamento)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val paymentValue = findViewById<TextView>(R.id.payment_value)
        val recipientName = findViewById<TextView>(R.id.recipient_name)
        val recipientCpf = findViewById<TextView>(R.id.recipient_cpf)
        val recipientInstitution = findViewById<TextView>(R.id.recipient_institution)
        val recipientKey = findViewById<TextView>(R.id.recipient_key)
        val btnPagar = findViewById<Button>(R.id.btnPagar)

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

        // Atualiza os campos na tela
        paymentValue.text = valorFormatado
        recipientKey.text = pixKey
        btnPagar.text = "Pagar $valorFormatado"

        btnBack.setOnClickListener {
            finish()
        }

        btnPagar.setOnClickListener {
            val intent = Intent(this, SenhaPagamentoActivity::class.java)
            intent.putExtra("PIX_KEY", pixKey)
            intent.putExtra("VALOR", valorString)
            startActivity(intent)
        }
    }
}
