package com.example.fluxbank

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificacaoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificacao)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnClearAll = findViewById<TextView>(R.id.btnClearAll)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewNotificacoes)

        btnBack.setOnClickListener {
            finish()
        }

        btnClearAll.setOnClickListener {
            Toast.makeText(this, "Notificações limpas!", Toast.LENGTH_SHORT).show()
            // Aqui você pode adicionar a lógica para limpar a lista de notificações
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        val notificacoes = listOf(
            Notificacao("Pix Recebido", "Você recebeu um Pix de R$ 50,00 de João da Silva."),
            Notificacao("Compra Aprovada", "Sua compra de R$ 120,00 na Americanas foi aprovada."),
            Notificacao("Fatura Fechada", "A fatura do seu cartão de crédito fechou. O valor é de R$ 450,00.")
        )

        recyclerView.adapter = NotificacaoAdapter(notificacoes)
    }
}