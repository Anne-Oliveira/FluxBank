package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CadastroEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_email)

        val btnClose = findViewById<ImageView>(R.id.btnClose)
        val btnNext = findViewById<ImageButton>(R.id.btnNextEmail)
        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)

        btnClose.setOnClickListener {
            finish() // Fecha a tela atual
        }

        btnNext.setOnClickListener {
            when {
                edtNome.text.toString().isEmpty() -> edtNome.error = "Nome é obrigatório"
                edtEmail.text.toString().isEmpty() -> edtEmail.error = "Email é obrigatório"
                else -> {
                    // A próxima tela será a CadastroSenhaActivity, que criarei a seguir
                    val intent = Intent(this, CadastroSenhaActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
