package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView

class CadastroEmailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_email)

        val btnClose = findViewById<ImageView>(R.id.btnClose)
        val btnNext = findViewById<ImageButton>(R.id.btnNextEmail)
        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)

        val cpf = intent.getStringExtra("cpf")
        val cnpj = intent.getStringExtra("cnpj")
        val tipoUsuario = intent.getStringExtra("tipoUsuario") ?: "PF"

        Log.d("CadastroEmail", "CPF: $cpf, CNPJ: $cnpj, Tipo: $tipoUsuario")

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
                    Log.d("CadastroEmail", "Nome: $nome, Email: $email")

                    val intent = Intent(this, CadastroSenhaTransActivity::class.java)
                    intent.putExtra("nome", nome)
                    intent.putExtra("email", email)
                    intent.putExtra("cpf", cpf)
                    intent.putExtra("cnpj", cnpj)
                    intent.putExtra("tipoUsuario", tipoUsuario)
                    startActivity(intent)
                }
            }
        }
    }
}