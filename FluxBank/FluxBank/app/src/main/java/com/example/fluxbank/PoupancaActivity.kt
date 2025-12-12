package com.example.fluxbank

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PoupancaActivity : BaseActivity() {

    private var isSaldoVisible = false
    private lateinit var adapter: PoupancaAdapter
    private lateinit var valorTotalValue: TextView

    private val poupancaItems = mutableListOf(
        PoupancaItem("Poupança Especial", "Ag/Conta: 1122/xx.xxxx-x", 3430.00, 3000.00),
        PoupancaItem("Poupança para Viagem", "Ag/Conta: 1122/xx.xxxx-y", 2000.00, 1500.00)
    )

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
        setContentView(R.layout.activity_poupanca)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        valorTotalValue = findViewById(R.id.valorTotalValue)
        val visibilityIcon = findViewById<ImageView>(R.id.visibilityIcon)

        btnBack.setOnClickListener {
            finish()
        }

        adapter = PoupancaAdapter(poupancaItems)
        val poupancaRecyclerView = findViewById<RecyclerView>(R.id.poupancaRecyclerView)
        poupancaRecyclerView.layoutManager = LinearLayoutManager(this)
        poupancaRecyclerView.adapter = adapter

        visibilityIcon.setOnClickListener {
            isSaldoVisible = !isSaldoVisible
            updateSaldoGeral()
            adapter.toggleSaldoVisibility()
        }

        updateSaldoGeral()
        setupBottomNavigation()
    }

    private fun updateSaldoGeral() {
        if (isSaldoVisible) {
            val total = poupancaItems.sumOf { it.totalValue }
            valorTotalValue.text = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("pt", "BR")).format(total)
        } else {
            valorTotalValue.text = "R$ ******"
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}