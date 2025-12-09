package com.example.fluxbank

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SegundaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segunda)

        val cpfRecebido = intent.getStringExtra("cpfDigitado")
        val txtCpfRecebido = findViewById<TextView>(R.id.txtCpfRecebido)

        txtCpfRecebido.text = "CPF: $cpfRecebido"
    }
}
