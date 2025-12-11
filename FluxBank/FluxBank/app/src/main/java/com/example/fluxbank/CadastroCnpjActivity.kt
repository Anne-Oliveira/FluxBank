package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView

class CadastroCnpjActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cnpj)

        val btnClose = findViewById<ImageView>(R.id.btnClose)
        val btnNext = findViewById<ImageButton>(R.id.btnNextCnpj)
        val edtCnpj = findViewById<EditText>(R.id.edtCnpj)

        val tipoUsuario = intent.getStringExtra("tipoUsuario") ?: "PJ"

        edtCnpj.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            private val mask = "##.###.###/####-##"

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) {
                    isUpdating = false
                    return
                }

                val digits = s.toString().replace(Regex("[^0-9]"), "")
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
                edtCnpj.setText(masked)
                edtCnpj.setSelection(masked.length)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnClose.setOnClickListener {
            finish()
        }

        btnNext.setOnClickListener {
            val cnpjMascarado = edtCnpj.text.toString()

            if (cnpjMascarado.isBlank() || cnpjMascarado.length < 18) { // 18 = XX.XXX.XXX/XXXX-XX
                edtCnpj.error = "CNPJ inválido"
            } else {
                val cnpjLimpo = cnpjMascarado.replace(Regex("[^0-9]"), "")

                Log.d("CadastroCnpj", "CNPJ limpo: $cnpjLimpo")

                val intent = Intent(this, CadastroEmailActivity::class.java)
                intent.putExtra("cnpj", cnpjLimpo)
                intent.putExtra("tipoUsuario", tipoUsuario)
                startActivity(intent)
            }
        }
    }
}