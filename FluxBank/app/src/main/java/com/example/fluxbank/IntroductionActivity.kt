package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class IntroductionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)

        val btnAbrirConta = findViewById<Button>(R.id.btnAbrirConta)
        val btnJaTenhoConta = findViewById<Button>(R.id.btnJaTenhoConta)

        btnAbrirConta.setOnClickListener {
            val intent = Intent(this, CadastroCpfActivity::class.java)
            startActivity(intent)
        }

        btnJaTenhoConta.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
