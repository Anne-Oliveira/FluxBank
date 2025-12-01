package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Configuração da Lista de Atividades Recentes
        setupRecyclerView()

        // Configuração dos botões de ação
        setupActionButtons()

        // Configuração dos cliques da Barra de Navegação
        setupBottomNavigation()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recentActivityList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val activities = listOf(
            RecentActivity("Ivan", "Pix Recebido", "R$2.000"),
            RecentActivity("Mimic Fornecimento", "Transferência", "R$2.000"),
            RecentActivity("Ivan", "Pix", "R$2.000")
        )

        val adapter = RecentActivityAdapter(activities)
        recyclerView.adapter = adapter
    }

    private fun setupActionButtons() {
        val btnPix = findViewById<MaterialCardView>(R.id.btn_pix)

        btnPix.setOnClickListener {
            val intent = Intent(this, PixActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupBottomNavigation() {
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navList = findViewById<ImageView>(R.id.nav_list)
        val navQr = findViewById<ImageView>(R.id.nav_qr)
        val navTransfer = findViewById<ImageView>(R.id.nav_transfer)
        val navSettings = findViewById<ImageView>(R.id.nav_settings)

        navHome.setOnClickListener {
            showToast("Início clicado")
        }
        navList.setOnClickListener {
            showToast("Lista clicado")
        }
        navQr.setOnClickListener {
            showToast("QR Code clicado")
        }
        navTransfer.setOnClickListener {
            showToast("Transferir clicado")
        }
        navSettings.setOnClickListener {
            showToast("Configurações clicado")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}