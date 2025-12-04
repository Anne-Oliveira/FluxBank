package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DefinirValorPixActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_definir_valor_pix)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnContinuar = findViewById<Button>(R.id.btnContinuar)
        val edtValor = findViewById<EditText>(R.id.edtValor)

        val pixKey = intent.getStringExtra("PIX_KEY")

        btnBack.setOnClickListener {
            finish()
        }

        btnContinuar.setOnClickListener {
            val valor = edtValor.text.toString()
            if (valor.isNotEmpty()) {
                val intent = Intent(this, ConfirmarPagamentoActivity::class.java)
                intent.putExtra("PIX_KEY", pixKey)
                intent.putExtra("VALOR", valor)
                startActivity(intent)
            } else {
                edtValor.error = "Digite um valor"
            }
        }
    }
}
