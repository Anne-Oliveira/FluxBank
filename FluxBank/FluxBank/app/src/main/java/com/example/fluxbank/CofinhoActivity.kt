package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CofinhoActivity : BaseActivity() {

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

    private fun setupBottomNavigation() {
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navList = findViewById<ImageView>(R.id.nav_list)
        val navQr = findViewById<ImageView>(R.id.nav_qr)
        val navTransfer = findViewById<ImageView>(R.id.nav_transfer)
        val navSettings = findViewById<ImageView>(R.id.nav_settings)

        navHome.setOnClickListener {
            finish() // Volta para a home
        }

        navList.setOnClickListener {
            showToast("Lista clicado")
        }

        navQr.setOnClickListener {
            val intent = Intent(this, LeitorQrActivity::class.java)
            startActivity(intent)
        }

        navTransfer.setOnClickListener {
            showToast("Transferir clicado")
        }

        navSettings.setOnClickListener {
            val intent = Intent(this, ConfiguracoesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
