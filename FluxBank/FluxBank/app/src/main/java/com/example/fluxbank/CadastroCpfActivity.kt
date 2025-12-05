package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CadastroCpfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cpf)

        val btnClose = findViewById<ImageView>(R.id.btnClose)
        val btnNext = findViewById<ImageButton>(R.id.btnNextCpf)
        val edtCpf = findViewById<EditText>(R.id.edtCpf)

        btnClose.setOnClickListener {
            finish() // Fecha a tela de cadastro
        }

        btnNext.setOnClickListener {
            if (edtCpf.text.toString().isEmpty()) {
                edtCpf.error = "CPF é obrigatório"
            } else {
                val intent = Intent(this, CadastroEmailActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
