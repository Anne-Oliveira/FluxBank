package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SenhaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senha)

        val edtSenha = findViewById<EditText>(R.id.edtSenha)
        val btnNext = findViewById<ImageButton>(R.id.btnNext)

        btnNext.setOnClickListener {
            val senha = edtSenha.text.toString()

            if (senha.isEmpty()) {
                edtSenha.error = "Digite sua senha"
            } else {
                val intent = Intent(this, LoadingActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
