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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poupanca)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val valorTotalValue = findViewById<TextView>(R.id.valorTotalValue)
        val visibilityIcon = findViewById<ImageView>(R.id.visibilityIcon)

        btnBack.setOnClickListener {
            finish()
        }

        visibilityIcon.setOnClickListener {
            isSaldoVisible = !isSaldoVisible
            if (isSaldoVisible) {
                valorTotalValue.text = saldoVisivel
                visibilityIcon.setImageResource(R.drawable.ic_visibility_off)
            } else {
                valorTotalValue.text = saldoOculto
                visibilityIcon.setImageResource(R.drawable.ic_visibility)
            }
        }

        val poupancaRecyclerView = findViewById<RecyclerView>(R.id.poupancaRecyclerView)
        poupancaRecyclerView.layoutManager = LinearLayoutManager(this)

        val poupancaItems = listOf(
            PoupancaItem("Poupança Especial", "Ag/Conta: 1122/xx.xxxx-x", "R$ ****", "R$ ****"),
            PoupancaItem("Poupança Especial", "Ag/Conta: 1122/xx.xxxx-x", "R$ ****", "R$ ****")
        )

        val adapter = PoupancaAdapter(poupancaItems)
        poupancaRecyclerView.adapter = adapter
    }
}
