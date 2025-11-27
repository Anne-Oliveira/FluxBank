package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edtCpf = findViewById<EditText>(R.id.edtCpf)
        val btnNext = findViewById<ImageButton>(R.id.btnNext)

        btnNext.setOnClickListener {
            val cpf = edtCpf.text.toString()

            if (cpf.isEmpty()) {
                edtCpf.error = "Digite seu CPF"
            } else {
                val intent = Intent(this, SenhaActivity::class.java)
                intent.putExtra("cpfDigitado", cpf)
                startActivity(intent)
            }
        }
    }
}
