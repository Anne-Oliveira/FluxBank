package com.example.fluxbank

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EnviarFaqActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_envio_faq)

        val perguntaInput = findViewById<EditText>(R.id.search_input2)
        val enviarButton = findViewById<Button>(R.id.btnEnviarFaq)

        enviarButton.setOnClickListener {
            val pergunta = perguntaInput.text.toString().trim()

            if (pergunta.isEmpty()) {
                Toast.makeText(this, "Digite sua pergunta", Toast.LENGTH_SHORT).show()
            } else {
                // Aqui você pode enviar para o backend, salvar no Firebase etc.
                Toast.makeText(this, "Pergunta enviada com sucesso!", Toast.LENGTH_LONG).show()
                finish() // fecha a activity
            }
        }
    }
}
