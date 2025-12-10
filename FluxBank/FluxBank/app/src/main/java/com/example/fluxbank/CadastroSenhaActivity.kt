package com.example.fluxbank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class CadastroSenhaActivity : BaseActivity() {

    lateinit var regra8: TextView
    lateinit var regraMaiuscula: TextView
    lateinit var regraEspecial: TextView
    lateinit var regraCoincide: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_senha)

        val btnClose = findViewById<ImageView>(R.id.btnClose)
        val btnFinish = findViewById<ImageButton>(R.id.btnFinishCadastro)
        val edtSenha = findViewById<EditText>(R.id.edtSenha)
        val edtConfirmarSenha = findViewById<EditText>(R.id.edtConfirmarSenha)

        regra8 = findViewById(R.id.regra8)
        regraMaiuscula = findViewById(R.id.regraMaiuscula)
        regraEspecial = findViewById(R.id.regraEspecial)
        regraCoincide = findViewById(R.id.regraCoincide)

        btnClose.setOnClickListener { finish() }

        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validarRegras(edtSenha.text.toString(), edtConfirmarSenha.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        edtSenha.addTextChangedListener(watcher)
        edtConfirmarSenha.addTextChangedListener(watcher)

        btnFinish.setOnClickListener {
            val senha = edtSenha.text.toString()
            val confirmarSenha = edtConfirmarSenha.text.toString()

            if (validarRegras(senha, confirmarSenha)) {
                // Adiciona a nova senha ao conjunto de senhas válidas
                addPasswordToSet(senha)

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun validarRegras(senha: String, confirmar: String): Boolean {
        var valido = true

        if (senha.length >= 8) regra8.setTextColor(0xFF00AA00.toInt())
        else { regra8.setTextColor(0xFFFF0000.toInt()); valido = false }

        if (senha.any { it.isUpperCase() }) regraMaiuscula.setTextColor(0xFF00AA00.toInt())
        else { regraMaiuscula.setTextColor(0xFFFF0000.toInt()); valido = false }

        val especiais = "!@#$%&*"
        if (senha.any { it in especiais }) regraEspecial.setTextColor(0xFF00AA00.toInt())
        else { regraEspecial.setTextColor(0xFFFF0000.toInt()); valido = false }

        if (senha == confirmar && senha.isNotEmpty()) regraCoincide.setTextColor(0xFF00AA00.toInt())
        else { regraCoincide.setTextColor(0xFFFF0000.toInt()); valido = false }

        return valido
    }

    private fun addPasswordToSet(password: String) {
        val prefs = getSharedPreferences("fluxbank_prefs", Context.MODE_PRIVATE)
        val passwords = prefs.getStringSet("user_passwords", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        passwords.add(password)
        with(prefs.edit()) {
            putStringSet("user_passwords", passwords)
            apply()
        }
    }
}
