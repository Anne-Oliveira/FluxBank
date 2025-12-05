package com.example.fluxbank

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.fluxbank.R

class ExtratoActivity : AppCompatActivity() {

    private lateinit var saldoLabel: TextView
    private lateinit var saldoValue: TextView
    private lateinit var visibilityIcon: ImageView
    private lateinit var labelTransferir: TextView
    private lateinit var histInput: EditText
    private lateinit var listViewExtrato: ListView

    private lateinit var navHome: ImageView
    private lateinit var navList: ImageView
    private lateinit var navTransfer: ImageView
    private lateinit var navSettings: ImageView
    private lateinit var navQr: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extrato)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        saldoLabel = findViewById(R.id.saldoLabel)
        saldoValue = findViewById(R.id.saldoValue)
        visibilityIcon = findViewById(R.id.visibilityIcon)
        labelTransferir = findViewById(R.id.labelTransferir)
        histInput = findViewById(R.id.hist_input)
        listViewExtrato = findViewById(R.id.listViewExtrato)

        navHome = findViewById(R.id.nav_home)
        navList = findViewById(R.id.nav_list)
        navTransfer = findViewById(R.id.nav_transfer)
        navSettings = findViewById(R.id.nav_settings)
        navQr = findViewById(R.id.nav_qr)

        visibilityIcon.setOnClickListener {
            saldoValue.text = if (saldoValue.text == "R$********") "R$ 1.258,90" else "R$********"
        }

        val lista = listOf(
            ExtratoItem("OUT 15", "PIX enviado", "Laura Alessio", "-R$ 7,00", R.drawable.ic_porquinho),
            ExtratoItem("OUT 15", "PIX enviado", "Laura Alessio", "-R$ 7,00", R.drawable.ic_porquinho),
            ExtratoItem("OUT 15", "PIX enviado", "Laura Alessio", "-R$ 7,00", R.drawable.ic_porquinho),
            ExtratoItem("OUT 15", "PIX enviado", "Laura Alessio", "-R$ 7,00", R.drawable.ic_porquinho),
            ExtratoItem("OUT 15", "PIX enviado", "Laura Alessio", "-R$ 7,00", R.drawable.ic_porquinho),
            ExtratoItem("OUT 15", "PIX enviado", "Laura Alessio", "-R$ 7,00", R.drawable.ic_porquinho)
        )

        val adapter = ExtratoAdapter(this, lista)
        listViewExtrato.adapter = adapter

        navHome.setOnClickListener { Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show() }
        navList.setOnClickListener { Toast.makeText(this, "Listagem", Toast.LENGTH_SHORT).show() }
        navTransfer.setOnClickListener { Toast.makeText(this, "Transferir", Toast.LENGTH_SHORT).show() }
        navSettings.setOnClickListener { Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show() }
        navQr.setOnClickListener { Toast.makeText(this, "QR Code", Toast.LENGTH_SHORT).show() }
    }
}
