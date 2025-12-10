package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*

class ExtratoActivity : BaseActivity() {

    private lateinit var saldoLabel: TextView
    private lateinit var saldoValue: TextView
    private lateinit var visibilityIcon: ImageView
    private lateinit var labelTransferir: TextView
    private lateinit var histInput: EditText
    private lateinit var listViewExtrato: ListView

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

        setupBottomNavigation()
    }
}
