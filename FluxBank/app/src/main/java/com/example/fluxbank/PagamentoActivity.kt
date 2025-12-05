package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PagamentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento) // seu XML

        // Header shortcuts
        val comprovantesShortcut = findViewById<LinearLayout>(R.id.comprovantesShortcut)
        val faturaShortcut = findViewById<LinearLayout>(R.id.faturaShortcut)
        val gerenciarShortcut = findViewById<LinearLayout>(R.id.gerenciarShortcut)
        val maisOpcoesShortcut = findViewById<LinearLayout>(R.id.maisOpcoesShortcut)

        // Pix field + botão escanear
        val pixField = findViewById<EditText>(R.id.pixField)
        val btnScanBarcode = findViewById<Button>(R.id.btnScanBarcode)

        // Seções de pagamentos
        val meusBoletos = findViewById<LinearLayout>(R.id.meusBoletos)
        val veiculos = findViewById<LinearLayout>(R.id.veiculos)

        // Bottom navigation
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navList = findViewById<ImageView>(R.id.nav_list)
        val navTransfer = findViewById<ImageView>(R.id.nav_transfer)
        val navSettings = findViewById<ImageView>(R.id.nav_settings)
        val navQr = findViewById<ImageView>(R.id.nav_qr)

        // 🔹 Ações de clique
        comprovantesShortcut.setOnClickListener {
            Toast.makeText(this, "Abrindo comprovantes...", Toast.LENGTH_SHORT).show()
        }

        faturaShortcut.setOnClickListener {
            Toast.makeText(this, "Abrindo fatura do cartão...", Toast.LENGTH_SHORT).show()
        }

        gerenciarShortcut.setOnClickListener {
            Toast.makeText(this, "Gerenciar pagamentos...", Toast.LENGTH_SHORT).show()
        }

        maisOpcoesShortcut.setOnClickListener {
            Toast.makeText(this, "Mais opções...", Toast.LENGTH_SHORT).show()
        }

        btnScanBarcode.setOnClickListener {
            Toast.makeText(this, "Escanear código de barras...", Toast.LENGTH_SHORT).show()
            // Aqui você pode abrir uma Activity de scanner
        }

        meusBoletos.setOnClickListener {
            Toast.makeText(this, "Abrindo boletos...", Toast.LENGTH_SHORT).show()
        }

        veiculos.setOnClickListener {
            Toast.makeText(this, "Abrindo veículos (IPVA, multas)...", Toast.LENGTH_SHORT).show()
        }

        // Bottom nav
        navHome.setOnClickListener {
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
        }

        navList.setOnClickListener {
            Toast.makeText(this, "Lista", Toast.LENGTH_SHORT).show()
        }

        navTransfer.setOnClickListener {
            Toast.makeText(this, "Transferências", Toast.LENGTH_SHORT).show()
        }

        navSettings.setOnClickListener {
            Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show()
        }

        navQr.setOnClickListener {
            Toast.makeText(this, "Abrindo QR Code...", Toast.LENGTH_SHORT).show()
        }
    }
}
