package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView

class PagamentosActivity : BaseActivity() {

    private val titulos = listOf(
        "Comprovante",
        "Fatura do cartão",
        "Gerenciar Pags.",
        "Mais opções"
    )

    private val icones = listOf(
        R.drawable.ic_docs,
        R.drawable.ic_cartao,
        R.drawable.ic_money,
        R.drawable.ic_more
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagar)

        // GRID
        val listView = findViewById<GridView>(R.id.listPagamentos)
        listView.adapter = PagamentoAdapter()

        // BOTÃO VOLTAR
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Configurar a barra de navegação
        setupBottomNavigation()
    }

    inner class PagamentoAdapter : BaseAdapter() {
        override fun getCount() = titulos.size
        override fun getItem(position: Int) = titulos[position]
        override fun getItemId(position: Int) = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = layoutInflater.inflate(R.layout.list_item_pagamento, parent, false)

            val icon = view.findViewById<ImageView>(R.id.item_icon)
            val title = view.findViewById<TextView>(R.id.item_title)

            icon.setImageResource(icones[position])
            title.text = titulos[position]

            return view
        }
    }
}
