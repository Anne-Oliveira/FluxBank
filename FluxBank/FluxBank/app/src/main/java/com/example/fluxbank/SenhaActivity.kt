package com.example.fluxbank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class SenhaActivity : BaseActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senha)

        val edtSenha = findViewById<EditText>(R.id.edtSenha)
        val btnNext = findViewById<ImageButton>(R.id.btnNext)
        val chkBiometria = findViewById<CheckBox>(R.id.chkBiometria)
        val txtEsqueciSenha = findViewById<TextView>(R.id.txtEsqueciSenha)

        // Inicializa a biometria
        setupBiometrics()

        // Verifica se a biometria já foi ativada
        if (isBiometricEnabled()) {
            chkBiometria.isChecked = true
            biometricPrompt.authenticate(promptInfo)
        }

        txtEsqueciSenha.setOnClickListener {
            startActivity(Intent(this, EsqueciSenhaActivity::class.java))
        }

        btnNext.setOnClickListener {
            val senha = edtSenha.text.toString()

            if (senha.isEmpty()) {
                edtSenha.error = "Digite sua senha"
                return@setOnClickListener
            }

            if (isValidPassword(senha)) {
                saveBiometricPreference(chkBiometria.isChecked)
                navigateToLoadingScreen()
            } else {
                edtSenha.error = "Senha incorreta"
            }
        }
    }

    private fun setupBiometrics() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                navigateToLoadingScreen()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON && errorCode != BiometricPrompt.ERROR_USER_CANCELED) {
                    showToast("Erro de autenticação: $errString")
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                showToast("Falha na autenticação biométrica")
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login com Biometria")
            .setSubtitle("Use sua digital para entrar")
            .setNegativeButtonText("Usar senha")
            .build()
    }

    private fun isBiometricEnabled(): Boolean {
        val prefs = getSharedPreferences("fluxbank_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("biometric_enabled", false) && canAuthenticateWithBiometrics()
    }

    private fun saveBiometricPreference(isEnabled: Boolean) {
        val prefs = getSharedPreferences("fluxbank_prefs", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            if (canAuthenticateWithBiometrics()) {
                putBoolean("biometric_enabled", isEnabled)
                apply()
            } else if (!isEnabled) {
                putBoolean("biometric_enabled", false)
                apply()
            }
        }
    }

    private fun canAuthenticateWithBiometrics(): Boolean {
        val biometricManager = BiometricManager.from(this)
        val canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)
        return canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS
    }

    private fun isValidPassword(password: String): Boolean {
        // Validação de senha temporariamente desativada
        return true
    }

    private fun navigateToLoadingScreen() {
        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}