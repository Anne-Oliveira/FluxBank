package com.example.fluxbank

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CofinhoActivity : BaseActivity() {

    private var isSaldoVisible = false
    private val saldoOculto = "R$ ******"
    private val saldoVisivel = "R$ 150,00"

    override fun onCreate(savedInstanceState: Bundle?) {
        // Aplica tema baseado no documento recebido via Intent antes de inflar views
        val documento = intent?.getStringExtra("documento")
        val isCNPJ = documento?.length == 14
        val isCPF = documento?.length == 11

        when {
            isCNPJ -> setTheme(R.style.ThemeOverlay_FluxBank_CNPJ)
            isCPF -> setTheme(R.style.ThemeOverlay_FluxBank_CPF)
            else -> setTheme(R.style.ThemeOverlay_FluxBank_CPF)
        }

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

        // Configurar bottom navigation
        setupBottomNavigation()
    }

    private fun setupCofinho() {
        val cofinhoRecyclerView = findViewById<RecyclerView>(R.id.cofinhoRecyclerView)
        cofinhoRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val cofinhoItems = listOf(
            CofinhoItem(R.drawable.ic_porquinho, "Dia a Dia", "R$ 0,00", "Rendeu R$ 0,00"),
            CofinhoItem(R.drawable.ic_travel_bag, "Viagem", "R$ 0,00", "Rendeu R$ 0,00")
        )

        val adapter = CofinhoAdapter(cofinhoItems)
        cofinhoRecyclerView.adapter = adapter
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
