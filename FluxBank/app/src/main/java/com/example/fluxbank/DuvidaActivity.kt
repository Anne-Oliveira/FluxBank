package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DuvidaActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnSearch: ImageButton
    private lateinit var sendDuvida: EditText
    private lateinit var btnSendDuvida: Button

    // Bottom navigation
    private lateinit var navHome: ImageView
    private lateinit var navList: ImageView
    private lateinit var navTransfer: ImageView
    private lateinit var navSettings: ImageView
    private lateinit var navQr: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_duvida)

        // -----------------------------
        // Cabeçalho
        // -----------------------------
        btnBack = findViewById(R.id.btnBack)
        btnSearch = findViewById(R.id.btn_search)

        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSearch.setOnClickListener {
            Toast.makeText(this, "Ajuda clicada", Toast.LENGTH_SHORT).show()
        }

        // -----------------------------
        // Envio de dúvida
        // -----------------------------
        sendDuvida = findViewById(R.id.sendDuvida)
        btnSendDuvida = findViewById(R.id.btnSendDuvida)

        btnSendDuvida.setOnClickListener {
            val texto = sendDuvida.text.toString().trim()

            if (texto.isEmpty()) {
                Toast.makeText(this, "Digite sua dúvida primeiro", Toast.LENGTH_SHORT).show()
            } else {
                mostrarPopupDuvida(texto)
                sendDuvida.text.clear()
            }
        }

        // -----------------------------
        // Bottom Navigation
        // -----------------------------
        navHome = findViewById(R.id.nav_home)
        navList = findViewById(R.id.nav_list)
        navTransfer = findViewById(R.id.nav_transfer)
        navSettings = findViewById(R.id.nav_settings)
        navQr = findViewById(R.id.nav_qr)

        navHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        navList.setOnClickListener {
            Toast.makeText(this, "Extrato", Toast.LENGTH_SHORT).show()
        }

        navTransfer.setOnClickListener {
            Toast.makeText(this, "Transferências", Toast.LENGTH_SHORT).show()
        }

        navSettings.setOnClickListener {
            Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show()
        }

        navQr.setOnClickListener {
            Toast.makeText(this, "QR Code", Toast.LENGTH_SHORT).show()
        }
    }

    // -----------------------------
    // POP-UP de confirmação
    // -----------------------------
    private fun mostrarPopupDuvida(texto: String) {
        AlertDialog.Builder(this)
            .setTitle("Dúvida enviada")
            .setMessage("Sua pergunta foi enviada:\n\n$texto")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()

                // Voltar para a HomeActivity
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            .show()
    }
}