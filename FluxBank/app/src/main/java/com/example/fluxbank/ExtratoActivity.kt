package com.seuprojeto.fluxbank

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.fluxbank.R


class FaqActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)

        // Cabeçalho
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        val btnSearch: ImageButton = findViewById(R.id.btn_search)
        val titleExtrato: TextView = findViewById(R.id.titleExtrato)

        btnBack.setOnClickListener {
            finish() // fecha a Activity
        }

        btnSearch.setOnClickListener {
            Toast.makeText(this, "Ajuda clicada", Toast.LENGTH_SHORT).show()
            // aqui você pode abrir outra tela de ajuda
        }

        // Saldo
        val saldoLabel: TextView = findViewById(R.id.saldoLabel)
        val saldoValue: TextView = findViewById(R.id.saldoValue)
        val visibilityIcon: ImageView = findViewById(R.id.visibilityIcon)

        var saldoVisivel = true
        visibilityIcon.setOnClickListener {
            saldoVisivel = !saldoVisivel
            saldoValue.text = if (saldoVisivel) "R$ 1.234,56" else "R$ ********"
        }

        // Campo de busca
        val searchExtrato: EditText = findViewById(R.id.searchExtrato)
        searchExtrato.setOnEditorActionListener { _, _, _ ->
            val query = searchExtrato.text.toString()
            Toast.makeText(this, "Buscando: $query", Toast.LENGTH_SHORT).show()
            true
        }

        // Histórico
        val amount1: TextView = findViewById(R.id.amount)
        amount1.setOnClickListener {
            Toast.makeText(this, "Transação 1 clicada", Toast.LENGTH_SHORT).show()
        }

        // Bottom Navigation
        val navHome: ImageView = findViewById(R.id.nav_home)
        val navList: ImageView = findViewById(R.id.nav_list)
        val navTransfer: ImageView = findViewById(R.id.nav_transfer)
        val navSettings: ImageView = findViewById(R.id.nav_settings)
        val navQr: ImageView = findViewById(R.id.nav_qr)

        navHome.setOnClickListener {
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
        }
        navList.setOnClickListener {
            Toast.makeText(this, "Extrato", Toast.LENGTH_SHORT).show()
        }
        navTransfer.setOnClickListener {
            Toast.makeText(this, "Transferência", Toast.LENGTH_SHORT).show()
        }
        navSettings.setOnClickListener {
            Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show()
        }
        navQr.setOnClickListener {
            Toast.makeText(this, "QR Code", Toast.LENGTH_SHORT).show()
        }
    }
}