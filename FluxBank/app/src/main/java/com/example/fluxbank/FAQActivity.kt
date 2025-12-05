package com.example.fluxbank

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import android.content.Intent

class FaqActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnSearch: ImageButton
    private lateinit var searchFaq: EditText
    private lateinit var btnSendQuestion: Button



    // Perguntas e respostas
    private lateinit var btnExpand1: ImageButton
    private lateinit var answer1: TextView

    private lateinit var btnExpand2: ImageButton
    private lateinit var answer2: TextView

    private lateinit var btnExpand3: ImageButton
    private lateinit var answer3: TextView

    private lateinit var btnExpand4: ImageButton
    private lateinit var answer4: TextView

    // Bottom navigation
    private lateinit var navHome: ImageView
    private lateinit var navList: ImageView
    private lateinit var navTransfer: ImageView
    private lateinit var navSettings: ImageView
    private lateinit var navQr: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)

        // Cabeçalho
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()}
        btnSearch = findViewById(R.id.btn_search)

        btnBack.setOnClickListener { finish() }
        btnSearch.setOnClickListener {
            Toast.makeText(this, "Ajuda clicada", Toast.LENGTH_SHORT).show()
        }

        // Campo de busca
        searchFaq = findViewById(R.id.searchFaq)

        // Botão enviar pergunta
        btnSendQuestion = findViewById(R.id.btnSendQuestion)
        btnSendQuestion.setOnClickListener {
            val intent = Intent(this, DuvidaActivity::class.java)
            startActivity(intent)
        }

        // Expandir respostas
        btnExpand1 = findViewById(R.id.btnExpand1)
        answer1 = findViewById(R.id.answer1)
        btnExpand1.setOnClickListener { toggleVisibility(answer1) }

        btnExpand2 = findViewById(R.id.btnExpand2)
        answer2 = findViewById(R.id.answer2)
        btnExpand2.setOnClickListener { toggleVisibility(answer2) }

        btnExpand3 = findViewById(R.id.btnExpand3)
        answer3 = findViewById(R.id.answer3)
        btnExpand3.setOnClickListener { toggleVisibility(answer3) }

        btnExpand4 = findViewById(R.id.btnExpand4)
        answer4 = findViewById(R.id.answer4)
        btnExpand4.setOnClickListener { toggleVisibility(answer4) }

        // Bottom navigation
        navHome = findViewById(R.id.nav_home)
        navList = findViewById(R.id.nav_list)
        navTransfer = findViewById(R.id.nav_transfer)
        navSettings = findViewById(R.id.nav_settings)
        navQr = findViewById(R.id.nav_qr)

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

    private fun toggleVisibility(view: TextView) {
        if (view.visibility == View.GONE) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}
