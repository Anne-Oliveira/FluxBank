package com.example.fluxbank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton

class SenhaActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senha)

        val edtSenha = findViewById<EditText>(R.id.edtSenha)
        val btnNext = findViewById<ImageButton>(R.id.btnNext)
        val chkBiometria = findViewById<CheckBox>(R.id.chkBiometria)

        // Carrega a preferência de biometria para o checkbox
        chkBiometria.isChecked = isBiometricPreferenceEnabled()

        btnNext.setOnClickListener {
            val senha = edtSenha.text.toString()

            if (senha.isEmpty()) {
                edtSenha.error = "Digite sua senha"
                return@setOnClickListener
            }

            // A validação de senha está temporariamente desativada
            if (isValidPassword(senha)) {
                // Salva a preferência de biometria se a caixa estiver marcada
                saveBiometricPreference(chkBiometria.isChecked)

                // Navega para a próxima tela
                navigateToLoadingScreen()
            } else {
                edtSenha.error = "Senha incorreta"
            }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        // Validação de senha temporariamente desativada a pedido do usuário.
        return true
    }

    private fun saveBiometricPreference(isEnabled: Boolean) {
        val prefs = getSharedPreferences("fluxbank_prefs", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putBoolean("biometric_enabled", isEnabled)
            apply()
        }
    }

    private fun isBiometricPreferenceEnabled(): Boolean {
        val prefs = getSharedPreferences("fluxbank_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("biometric_enabled", false)
    }

    private fun navigateToLoadingScreen() {
        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)
        finish()
    }
}
