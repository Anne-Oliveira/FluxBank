package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "=== onCreate iniciado ===")

        val edtCpf = findViewById<EditText>(R.id.edtCpf)
        val btnNext = findViewById<ImageButton>(R.id.btnNext)


        edtCpf.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) return

                val digits = s.toString().filter { it.isDigit() }

                // Decide se é CPF ou CNPJ
                val mask = when {
                    digits.length <= 11 -> "###.###.###-##"
                    else -> "##.###.###/####-##"
                }

                var masked = ""
                var index = 0

                for (m in mask) {
                    if (m == '#') {
                        if (index < digits.length) {
                            masked += digits[index]
                            index++
                        } else break
                    } else {
                        if (index <= digits.length) masked += m
                    }
                }

                isUpdating = true
                edtCpf.setText(masked)
                edtCpf.setSelection(masked.length)
                isUpdating = false
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnNext.setOnClickListener {
            val documentoMascarado = edtCpf.text.toString()
            val documentoLimpo = documentoMascarado.filter { it.isDigit() }

            Log.d("MainActivity", "Documento mascarado: $documentoMascarado")
            Log.d("MainActivity", "Documento limpo: $documentoLimpo")

            when (documentoLimpo.length) {

                11 -> {
                    Log.d("MainActivity", "Detectado CPF válido")
                    navegarParaSenha(documentoLimpo)
                }

                14 -> {
                    Log.d("MainActivity", "Detectado CNPJ válido")
                    navegarParaSenha(documentoLimpo)
                }

                else -> {
                    Log.d("MainActivity", "Documento inválido: tamanho ${documentoLimpo.length}")
                    edtCpf.error = "Digite um CPF ou CNPJ válido"
                    Toast.makeText(
                        this,
                        "CPF deve ter 11 dígitos — CNPJ deve ter 14",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun navegarParaSenha(documento: String) {
        Toast.makeText(this, "Enviando documento: $documento", Toast.LENGTH_LONG).show()

        Log.d("MainActivity", "=== Criando Intent ===")
        Log.d("MainActivity", "Documento enviado: $documento")

        val intent = Intent(this, SenhaActivity::class.java)
        intent.putExtra("documento", documento)

        startActivity(intent)
    }
}
