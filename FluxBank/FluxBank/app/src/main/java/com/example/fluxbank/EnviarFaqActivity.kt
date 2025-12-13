package com.example.fluxbank

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EnviarFaqActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val documento = intent?.getStringExtra("documento")
        val isCNPJ = documento?.length == 14
        val isCPF = documento?.length == 11

        when {
            isCNPJ -> setTheme(R.style.ThemeOverlay_FluxBank_CNPJ)
            isCPF -> setTheme(R.style.ThemeOverlay_FluxBank_CPF)
            else -> setTheme(R.style.ThemeOverlay_FluxBank_CPF)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_envio_faq)

        val perguntaInput = findViewById<EditText>(R.id.search_input2)
        val enviarButton = findViewById<Button>(R.id.btnEnviarFaq)
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        enviarButton.setOnClickListener {
            val pergunta = perguntaInput.text.toString().trim()

            if (pergunta.isEmpty()) {
                Toast.makeText(this, "Digite sua pergunta", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Pergunta enviada com sucesso!", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}
