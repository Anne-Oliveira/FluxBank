package com.example.fluxbank

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EsqueciSenhaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esqueci_senha)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnEnviarLink = findViewById<Button>(R.id.btnEnviarLink)

        btnBack.setOnClickListener {
            finish()
        }

        btnEnviarLink.setOnClickListener {
            Toast.makeText(this, "Funcionalidade em construção!", Toast.LENGTH_SHORT).show()
        }
    }
}