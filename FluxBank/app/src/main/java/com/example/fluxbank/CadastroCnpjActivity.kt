package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CadastroCnpjActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cnpj)

        val btnClose = findViewById<ImageView>(R.id.btnClose)
        val btnNext = findViewById<ImageButton>(R.id.btnNextCnpj)
        val edtCnpj = findViewById<EditText>(R.id.edtCnpj)

        btnClose.setOnClickListener {
            finish() // Fecha a tela de cadastro
        }

        btnNext.setOnClickListener {
            if (edtCnpj.text.toString().isEmpty()) {
                edtCnpj.error = "CNPJ é obrigatório"
            } else {
                val intent = Intent(this, CadastroEmailActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
