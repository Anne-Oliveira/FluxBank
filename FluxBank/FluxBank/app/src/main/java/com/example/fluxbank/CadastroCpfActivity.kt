package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CadastroCpfActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cpf)

        val btnClose = findViewById<ImageView>(R.id.btnClose)
        val btnNext = findViewById<ImageButton>(R.id.btnNextCpf)
        val edtCpf = findViewById<EditText>(R.id.edtCpf)

        // 🔹 Aplica máscara de CPF
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

        btnClose.setOnClickListener {
            finish()
        }

        btnNext.setOnClickListener {
            val cpf = edtCpf.text.toString()

            if (cpf.isBlank() || cpf.length < 14) { // 14 = 000.000.000-00
                edtCpf.error = "CPF inválido"
            } else {
                startActivity(Intent(this, CadastroEmailActivity::class.java))
            }
        }
    }
}
