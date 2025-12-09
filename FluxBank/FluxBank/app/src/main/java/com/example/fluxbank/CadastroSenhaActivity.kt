package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CadastroSenhaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_senha)

        val btnClose = findViewById<ImageView>(R.id.btnClose)
        val btnFinish = findViewById<ImageButton>(R.id.btnFinishCadastro)
        val edtSenha = findViewById<EditText>(R.id.edtSenha)
        val edtConfirmarSenha = findViewById<EditText>(R.id.edtConfirmarSenha)

        btnClose.setOnClickListener {
            finish()
        }

        btnFinish.setOnClickListener {
            val senha = edtSenha.text.toString()
            val confirmarSenha = edtConfirmarSenha.text.toString()

            when {
                senha.isEmpty() -> edtSenha.error = "Senha é obrigatória"
                confirmarSenha.isEmpty() -> edtConfirmarSenha.error = "Confirme sua senha"
                senha != confirmarSenha -> edtConfirmarSenha.error = "As senhas não coincidem"
                else -> {
                    // Navega para a tela de Login (CPF), finalizando o fluxo de cadastro
                    val intent = Intent(this, MainActivity::class.java)
                    // Limpa todas as telas anteriores do backstack para que o usuário não volte ao cadastro
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }
    }
}
