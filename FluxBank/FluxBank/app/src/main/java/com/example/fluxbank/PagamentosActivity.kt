package com.example.fluxbank

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

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

    private lateinit var searchInput: EditText

    private val barcodeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val barcode = data?.getStringExtra("barcode_data")
                searchInput.setText(barcode)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        val documento = intent?.getStringExtra("documento")
        val isCNPJ = documento?.length == 14
        val isCPF = documento?.length == 11

        when {
            isCNPJ -> setTheme(R.style.ThemeOverlay_FluxBank_CNPJ)
            isCPF -> setTheme(R.style.ThemeOverlay_FluxBank_CPF)
            else -> setTheme(R.style.ThemeOverlay_FluxBank_CPF)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagar)

        searchInput = findViewById(R.id.search_input)
        val listView = findViewById<GridView>(R.id.listPagamentos)
        listView.adapter = PagamentoAdapter()

        listView.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> { // Posição do ícone "Comprovante"
                    val intent = Intent(this, ComprovantesActivity::class.java)
                    startActivity(intent)
                }
                1 -> { // Posição do ícone "Fatura do cartão"
                    val intent = Intent(this, InvoiceActivity::class.java)
                    startActivity(intent)
                }
                2 -> { // Posição do ícone "Gerenciar Pags."
                    val intent = Intent(this, AgendarPagamentoActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        val btnEscanear = findViewById<Button>(R.id.btnEscanearCodigoDeBarras)
        btnEscanear.setOnClickListener {
            val intent = Intent(this, BarcodeScannerActivity::class.java)
            barcodeLauncher.launch(intent)
        }

        val btnBoleto = findViewById<LinearLayout>(R.id.bolItem)
        btnBoleto.setOnClickListener {
            val intent = Intent(this, BoletoActivity::class.java)
            startActivity(intent)
        }

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