package com.example.fluxbank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RedefinirSenhaActivity : AppCompatActivity() {

    private lateinit var edtNovaSenha: EditText
    private lateinit var edtConfirmarNovaSenha: EditText
    private lateinit var fabConfirmar: FloatingActionButton

    private lateinit var regra8: TextView
    private lateinit var regraMaiuscula: TextView
    private lateinit var regraNumero: TextView
    private lateinit var regraEspecial: TextView
    private lateinit var regraCoincide: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redefinir_senha)

        edtNovaSenha = findViewById(R.id.edtNovaSenha)
        edtConfirmarNovaSenha = findViewById(R.id.edtConfirmarNovaSenha)
        fabConfirmar = findViewById(R.id.fabConfirmar)

        regra8 = findViewById(R.id.regra8)
        regraMaiuscula = findViewById(R.id.regraMaiuscula)
        regraNumero = findViewById(R.id.regraNumero)
        regraEspecial = findViewById(R.id.regraEspecial)
        regraCoincide = findViewById(R.id.regraCoincide)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validarRegras(edtNovaSenha.text.toString(), edtConfirmarNovaSenha.text.toString())
            }
        }

        edtNovaSenha.addTextChangedListener(textWatcher)
        edtConfirmarNovaSenha.addTextChangedListener(textWatcher)

        fabConfirmar.setOnClickListener {
            val novaSenha = edtNovaSenha.text.toString()
            val confirmarSenha = edtConfirmarNovaSenha.text.toString()

            if (validarRegras(novaSenha, confirmarSenha)) {
                // Lógica para salvar a nova senha (substituindo a antiga)
                // Por enquanto, vamos assumir que a senha do usuário logado (se houver) é a que será alterada.
                // Em um app real, você teria que ter o identificador do usuário.
                saveNewPassword(novaSenha)

                Toast.makeText(this, "Senha redefinida com sucesso!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun validarRegras(senha: String, confirmar: String): Boolean {
        var valido = true

        if (senha.length >= 8) regra8.setTextColor(0xFF00AA00.toInt())
        else { regra8.setTextColor(0xFFFF0000.toInt()); valido = false }

        if (senha.any { it.isUpperCase() }) regraMaiuscula.setTextColor(0xFF00AA00.toInt())
        else { regraMaiuscula.setTextColor(0xFFFF0000.toInt()); valido = false }

        if (senha.any { it.isDigit() }) regraNumero.setTextColor(0xFF00AA00.toInt())
        else { regraNumero.setTextColor(0xFFFF0000.toInt()); valido = false }

        val especiais = "!@#$%&*"
        if (senha.any { it in especiais }) regraEspecial.setTextColor(0xFF00AA00.toInt())
        else { regraEspecial.setTextColor(0xFFFF0000.toInt()); valido = false }

        if (senha == confirmar && senha.isNotEmpty()) regraCoincide.setTextColor(0xFF00AA00.toInt())
        else { regraCoincide.setTextColor(0xFFFF0000.toInt()); valido = false }

        return valido
    }

    private fun saveNewPassword(newPass: String) {
        // Esta é uma lógica simplificada. Em um app real, você precisaria
        // identificar qual usuário está alterando a senha.
        val prefs = getSharedPreferences("fluxbank_prefs", Context.MODE_PRIVATE)
        val passwords = prefs.getStringSet("user_passwords", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        
        // Remove a senha antiga (se você tiver como identificá-la) e adiciona a nova.
        // Como não temos a senha antiga aqui, vamos apenas adicionar a nova.
        passwords.add(newPass)

        with(prefs.edit()) {
            putStringSet("user_passwords", passwords)
            apply()
        }
    }
}