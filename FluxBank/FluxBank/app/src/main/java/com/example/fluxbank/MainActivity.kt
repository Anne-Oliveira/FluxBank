package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

/**
 * Activity principal - Login (Parte 1: CPF/CNPJ)
 * Usuário digita CPF (11 dígitos) ou CNPJ (14 dígitos)
 * Mantém máscara de CPF original
 */
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edtCpf = findViewById<EditText>(R.id.edtCpf)
        val btnNext = findViewById<ImageButton>(R.id.btnNext)

        // Máscara de CPF (mantém funcionalidade original)
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

            if (cpfMascarado.isBlank() || cpfMascarado.length < 14) { // 14 = 000.000.000-00
                edtCpf.error = "Digite um CPF válido"
            } else {
                // Remove máscara para enviar apenas números para a API
                val cpfLimpo = cpfMascarado.replace(".", "").replace("-", "")
                // Validar se tem 11 dígitos (CPF)
                if (cpfLimpo.length == 11) {
                    navegarParaSenha(cpfLimpo)
                } else {
                    Toast.makeText(
                        this,
                        "CPF deve ter 11 dígitos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**
     * Navega para a tela de senha, passando o CPF sem máscara
     */
    private fun navegarParaSenha(cpf: String) {
        val intent = Intent(this, SenhaActivity::class.java)
        intent.putExtra("documento", cpf)  // Envia CPF limpo (só números)
        startActivity(intent)
    }
}