package com.example.fluxbank

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CofinhoActivity : AppCompatActivity() {

    private var isSaldoVisible = false
    private val saldoOculto = "R$ ******"
    private val saldoVisivel = "R$ 150,00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cofinho)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val totalCofinhoValue = findViewById<TextView>(R.id.totalCofinhoValue)
        val visibilityIcon = findViewById<ImageView>(R.id.visibilityIcon)

        // Define o estado inicial como oculto
        totalCofinhoValue.text = saldoOculto

        btnBack.setOnClickListener {
            finish()
        }

        visibilityIcon.setOnClickListener {
            isSaldoVisible = !isSaldoVisible
            if (isSaldoVisible) {
                totalCofinhoValue.text = saldoVisivel
                visibilityIcon.setImageResource(R.drawable.ic_visibility_off)
            } else {
                totalCofinhoValue.text = saldoOculto
                visibilityIcon.setImageResource(R.drawable.ic_visibility)
            }
        }

        setupCofinho()
    }

    private fun setupCofinho() {
        val cofinhoRecyclerView = findViewById<RecyclerView>(R.id.cofinhoRecyclerView)
        cofinhoRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val cofinhoItems = listOf(
            CofinhoItem(R.drawable.ic_piggy_bank, "Dia a Dia", "R$ 0,00", "Rendeu R$ 0,00"),
            CofinhoItem(R.drawable.ic_travel_bag, "Viagem", "R$ 0,00", "Rendeu R$ 0,00")
        )

        val adapter = CofinhoAdapter(cofinhoItems)
        cofinhoRecyclerView.adapter = adapter
    }
}
