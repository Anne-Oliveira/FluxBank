package com.example.fluxbank

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PoupancaActivity : AppCompatActivity() {

    private var isSaldoVisible = false
    private val saldoOculto = "R$ ******"
    private val saldoVisivel = "R$ 5.430,00"
    private lateinit var adapter: PoupancaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poupanca)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val valorTotalValue = findViewById<TextView>(R.id.valorTotalValue)
        val visibilityIcon = findViewById<ImageView>(R.id.visibilityIcon)

        btnBack.setOnClickListener {
            finish()
        }

        val poupancaItems = listOf(
            PoupancaItem("Poupança Especial", "Ag/Conta: 1122/xx.xxxx-x", "R$ 3.430,00", "R$ ****", "R$ 3.000,00", "R$ ****"),
            PoupancaItem("Poupança Especial", "Ag/Conta: 1122/xx.xxxx-x", "R$ 2.000,00", "R$ ****", "R$ 1.500,00", "R$ ****")
        )

        adapter = PoupancaAdapter(poupancaItems)
        val poupancaRecyclerView = findViewById<RecyclerView>(R.id.poupancaRecyclerView)
        poupancaRecyclerView.layoutManager = LinearLayoutManager(this)
        poupancaRecyclerView.adapter = adapter

        visibilityIcon.setOnClickListener {
            isSaldoVisible = !isSaldoVisible
            if (isSaldoVisible) {
                valorTotalValue.text = saldoVisivel
                visibilityIcon.setImageResource(R.drawable.ic_visibility_off)
            } else {
                valorTotalValue.text = saldoOculto
                visibilityIcon.setImageResource(R.drawable.ic_visibility)
            }
            adapter.toggleSaldoVisibility()
        }
    }
}
