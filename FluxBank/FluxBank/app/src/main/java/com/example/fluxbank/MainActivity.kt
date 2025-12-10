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
            private val mask = "###.###.###-##"

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) {
                    isUpdating = false
                    return
                }

                val digits = s.toString().replace(".", "").replace("-", "")
                var masked = ""
                var index = 0

                for (m in mask.toCharArray()) {
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
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnNext.setOnClickListener {
            val cpfMascarado = edtCpf.text.toString()

            Log.d("MainActivity", "=== Botão clicado ===")
            Log.d("MainActivity", "CPF com máscara: $cpfMascarado")

            if (cpfMascarado.isBlank() || cpfMascarado.length < 14) {
                edtCpf.error = "Digite um CPF válido"
                Log.d("MainActivity", "Erro: CPF inválido")
                return@setOnClickListener
            }

            val cpfLimpo = cpfMascarado.replace(".", "").replace("-", "")

            Log.d("MainActivity", "CPF limpo: $cpfLimpo")
            Log.d("MainActivity", "Tamanho: ${cpfLimpo.length} dígitos")

            if (cpfLimpo.length == 11) {
                Log.d("MainActivity", "✅ CPF válido! Navegando...")
                navegarParaSenha(cpfLimpo)
            } else {
                Log.d("MainActivity", "❌ CPF com tamanho incorreto")
                Toast.makeText(
                    this,
                    "CPF deve ter 11 dígitos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navegarParaSenha(cpf: String) {
        Toast.makeText(this, "Enviando: $cpf", Toast.LENGTH_LONG).show()
        Log.d("MainActivity", "=== Criando Intent ===")
        Log.d("MainActivity", "CPF a ser enviado: $cpf")

        val intent = Intent(this, SenhaActivity::class.java)
        intent.putExtra("documento", cpf)

        Log.d("MainActivity", "Intent criado com extra 'documento'")
        Log.d("MainActivity", "Iniciando SenhaActivity...")

        startActivity(intent)
    }
}