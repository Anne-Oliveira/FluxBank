package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CadastroEmailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_email)

        val btnClose = findViewById<ImageView>(R.id.btnClose)
        val btnNext = findViewById<ImageButton>(R.id.btnNextEmail)
        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)

        btnClose.setOnClickListener {
            finish()
        }

        btnNext.setOnClickListener {
            val nome = edtNome.text.toString().trim()
            val email = edtEmail.text.toString().trim()

            when {
                nome.isEmpty() -> edtNome.error = "Nome é obrigatório"

                email.isEmpty() -> edtEmail.error = "Email é obrigatório"

                !email.contains("@") -> edtEmail.error = "Email inválido: deve conter @"

                else -> {
                    val intent = Intent(this, CadastroSenhaActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
