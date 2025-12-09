package com.example.fluxbank

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificacaoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var adapter: NotificacaoAdapter
    private val notificacoes = mutableListOf<Notificacao>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificacao)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnClearAll = findViewById<TextView>(R.id.btnClearAll)
        recyclerView = findViewById(R.id.recyclerViewNotificacoes)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)

        btnBack.setOnClickListener {
            finish()
        }

        btnClearAll.setOnClickListener {
            notificacoes.clear()
            adapter.notifyDataSetChanged()
            updateEmptyState()
        }

        setupRecyclerView()
        carregarNotificacoes()
    }

    private fun setupRecyclerView() {
        adapter = NotificacaoAdapter(notificacoes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Adiciona o divisor
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, (recyclerView.layoutManager as LinearLayoutManager).orientation)
        val drawable = ContextCompat.getDrawable(this, R.drawable.divider_notificacao)
        drawable?.let { 
            dividerItemDecoration.setDrawable(it)
            recyclerView.addItemDecoration(dividerItemDecoration)
        }
    }

    private fun carregarNotificacoes() {
        // Simula o carregamento de notificações
        notificacoes.addAll(listOf(
            Notificacao("Pix Recebido", "Você recebeu um Pix de R$ 50,00 de João da Silva."),
            Notificacao("Compra Aprovada", "Sua compra de R$ 120,00 na Americanas foi aprovada."),
            Notificacao("Fatura Fechada", "A fatura do seu cartão de crédito fechou. O valor é de R$ 450,00.")
        ))
        adapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun updateEmptyState() {
        if (notificacoes.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyStateLayout.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyStateLayout.visibility = View.GONE
        }
    }
}