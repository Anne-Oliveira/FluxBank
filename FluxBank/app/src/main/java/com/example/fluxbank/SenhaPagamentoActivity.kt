package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SenhaPagamentoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senha_pagamento)

        val btnNext = findViewById<ImageButton>(R.id.btnNext)

        // Recebe os dados da tela anterior
        val pixKey = intent.getStringExtra("PIX_KEY")
        val valor = intent.getStringExtra("VALOR")

        btnNext.setOnClickListener {
            val intent = Intent(this, PagamentoSucessoActivity::class.java)
            intent.putExtra("PIX_KEY", pixKey)
            intent.putExtra("VALOR", valor)
            startActivity(intent)
            finish() // Encerra a tela de senha
        }
    }
}
