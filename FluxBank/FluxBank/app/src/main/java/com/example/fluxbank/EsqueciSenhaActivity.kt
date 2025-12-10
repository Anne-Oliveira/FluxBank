package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class EsqueciSenhaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esqueci_senha)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnRedefinirSenha = findViewById<Button>(R.id.btnEnviarLink)

        btnBack.setOnClickListener {
            finish()
        }

        btnRedefinirSenha.setOnClickListener {
            val intent = Intent(this, RedefinirSenhaActivity::class.java)
            startActivity(intent)
        }
    }
}