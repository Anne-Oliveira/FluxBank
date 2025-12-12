package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class CadastroSenhaTransActivity : BaseActivity() {

    private lateinit var regra6: TextView
    private lateinit var regraCoincide: TextView
    private lateinit var btnNext: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_senha_trans)

        val btnClose = findViewById<ImageView>(R.id.btnClose)
        btnNext = findViewById(R.id.btnNextSenhaTrans)
        val edtSenhaTrans = findViewById<EditText>(R.id.edtSenhaTrans)
        val edtConfirmarSenhaTrans = findViewById<EditText>(R.id.edtConfirmarSenhaTrans)

        regra6 = findViewById(R.id.regra6)
        regraCoincide = findViewById(R.id.regraCoincide)

        val nome = intent.getStringExtra("nome") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val cpf = intent.getStringExtra("cpf")
        val cnpj = intent.getStringExtra("cnpj")
        val tipoUsuario = intent.getStringExtra("tipoUsuario") ?: "PF"

        Log.d("CadastroSenhaTrans", "=== Dados recebidos ===")
        Log.d("CadastroSenhaTrans", "Nome: $nome")
        Log.d("CadastroSenhaTrans", "Email: $email")
        Log.d("CadastroSenhaTrans", "CPF: $cpf")
        Log.d("CadastroSenhaTrans", "CNPJ: $cnpj")
        Log.d("CadastroSenhaTrans", "Tipo: $tipoUsuario")

        btnClose.setOnClickListener { finish() }

        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validarRegras(edtSenhaTrans.text.toString(), edtConfirmarSenhaTrans.text.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        edtSenhaTrans.addTextChangedListener(watcher)
        edtConfirmarSenhaTrans.addTextChangedListener(watcher)

        btnNext.setOnClickListener {
            val senhaTrans = edtSenhaTrans.text.toString()
            val confirmarSenhaTrans = edtConfirmarSenhaTrans.text.toString()

            if (validarRegras(senhaTrans, confirmarSenhaTrans)) {
                val intentProximo = Intent(this, CadastroSenhaActivity::class.java)
                intentProximo.putExtra("nome", nome)
                intentProximo.putExtra("email", email)
                intentProximo.putExtra("cpf", cpf)
                intentProximo.putExtra("cnpj", cnpj)
                intentProximo.putExtra("tipoUsuario", tipoUsuario)
                intentProximo.putExtra("senhaTransacao", senhaTrans)
                startActivity(intentProximo)
            } else {
                Toast.makeText(
                    this,
                    "Corrija os erros antes de continuar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun validarRegras(senha: String, confirmar: String): Boolean {
        var valido = true

        if (senha.length == 6 && senha.all { it.isDigit() }) {
            regra6.setTextColor(0xFF00AA00.toInt())
        } else {
            regra6.setTextColor(0xFFFF0000.toInt())
            valido = false
        }

        if (senha == confirmar && senha.isNotEmpty()) {
            regraCoincide.setTextColor(0xFF00AA00.toInt())
        } else {
            regraCoincide.setTextColor(0xFFFF0000.toInt())
            valido = false
        }

        return valido
    }
}