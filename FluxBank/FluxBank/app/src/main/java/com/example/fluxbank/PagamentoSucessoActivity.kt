package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class PagamentoSucessoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento_sucesso)

        // Recebe os dados da tela anterior
        val pixKey = intent.getStringExtra("PIX_KEY")
        val valor = intent.getStringExtra("VALOR")

        // Aguarda 2 segundos e abre a tela de comprovante
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ComprovanteActivity::class.java)
            intent.putExtra("PIX_KEY", pixKey)
            intent.putExtra("VALOR", valor)
            startActivity(intent)
            finish()
        }, 2000) // 2000 milissegundos = 2 segundos
    }
}
