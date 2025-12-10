package com.example.fluxbank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.fluxbank.network.ApiClient
import com.example.fluxbank.network.models.LoginRequest
import com.example.fluxbank.utils.TokenManager
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class SenhaActivity : BaseActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var edtSenha: EditText
    private lateinit var btnNext: ImageButton
    private lateinit var chkBiometria: CheckBox
    private var documento: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senha)

        Log.d("SenhaActivity", "=== onCreate ===")

        tokenManager = TokenManager(this)

        documento = intent.getStringExtra("documento")
            ?: intent.getStringExtra("cpfDigitado")

        Log.d("SenhaActivity", "Documento: $documento")

        if (documento == null) {
            Toast.makeText(this, "Erro: CPF não recebido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        edtSenha = findViewById(R.id.edtSenha)
        btnNext = findViewById(R.id.btnNext)
        chkBiometria = findViewById(R.id.chkBiometria)
        chkBiometria.isChecked = isBiometricPreferenceEnabled()

        btnNext.setOnClickListener {
            val senha = edtSenha.text.toString()
            if (senha.isEmpty()) {
                edtSenha.error = "Digite sua senha"
                return@setOnClickListener
            }
            documento?.let { doc -> fazerLoginDebug(doc, senha) }
        }
    }

    private fun fazerLoginDebug(documento: String, senha: String) {
        btnNext.isEnabled = false

        lifecycleScope.launch {
            try {
                Log.d("SenhaActivity", "=== INÍCIO LOGIN DEBUG ===")
                Log.d("SenhaActivity", "URL: http://10.0.2.2:8080/api/auth/login")
                Log.d("SenhaActivity", "Documento: $documento")

                val request = LoginRequest(documento, senha)

                Log.d("SenhaActivity", "Fazendo requisição...")
                val response = ApiClient.api.login(request)

                Log.d("SenhaActivity", "Status Code: ${response.code()}")
                Log.d("SenhaActivity", "Success: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    val jsonString = response.body()?.toString() ?: "null"
                    Log.d("SenhaActivity", "=== JSON RETORNADO ===")
                    Log.d("SenhaActivity", jsonString)

                    val authResponse = response.body()

                    if (authResponse != null) {
                        Log.d("SenhaActivity", "✅ Parse OK!")
                        Log.d("SenhaActivity", "Token: ${authResponse.token.substring(0, 20)}...")
                        Log.d("SenhaActivity", "Usuario.nome: ${authResponse.usuario.nome}")
                        Log.d("SenhaActivity", "Usuario.cpf: ${authResponse.usuario.cpf}")
                        Log.d("SenhaActivity", "Usuario.email: ${authResponse.usuario.email}")
                        Log.d("SenhaActivity", "Usuario.contas: ${authResponse.usuario.contas?.size ?: 0}")

                        tokenManager.saveToken(authResponse.token)

                        val primeiraConta = authResponse.usuario.contas?.firstOrNull()
                        if (primeiraConta != null) {
                            Log.d("SenhaActivity", "Conta.id: ${primeiraConta.id}")
                            Log.d("SenhaActivity", "Conta.numeroConta: ${primeiraConta.numeroConta}")
                            Log.d("SenhaActivity", "Conta.saldo: ${primeiraConta.saldo}")
                        }

                        tokenManager.saveUserData(
                            userId = authResponse.usuario.id,
                            userName = authResponse.usuario.nome,
                            email = authResponse.usuario.email,
                            cpf = authResponse.usuario.cpf,
                            cnpj = authResponse.usuario.cnpj,
                            contaId = primeiraConta?.id,
                            numeroConta = primeiraConta?.numeroConta,
                            saldo = primeiraConta?.saldo?.toString()
                        )

                        saveBiometricPreference(chkBiometria.isChecked)

                        Toast.makeText(
                            this@SenhaActivity,
                            "Login OK! Usuário: ${authResponse.usuario.nome}",
                            Toast.LENGTH_SHORT
                        ).show()

                        navigateToLoadingScreen()
                    } else {
                        Log.e("SenhaActivity", "❌ Body é null!")
                        Toast.makeText(this@SenhaActivity, "Erro: Resposta vazia", Toast.LENGTH_LONG).show()
                        btnNext.isEnabled = true
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("SenhaActivity", "❌ Erro ${response.code()}")
                    Log.e("SenhaActivity", "ErrorBody: $errorBody")

                    Toast.makeText(
                        this@SenhaActivity,
                        "Erro: ${response.code()} - $errorBody",
                        Toast.LENGTH_LONG
                    ).show()
                    btnNext.isEnabled = true
                }

            } catch (e: Exception) {
                Log.e("SenhaActivity", "❌ EXCEÇÃO!", e)
                Log.e("SenhaActivity", "Tipo: ${e.javaClass.simpleName}")
                Log.e("SenhaActivity", "Mensagem: ${e.message}")
                Log.e("SenhaActivity", "Causa: ${e.cause}")

                e.printStackTrace()

                Toast.makeText(
                    this@SenhaActivity,
                    "ERRO: ${e.javaClass.simpleName}\n${e.message}",
                    Toast.LENGTH_LONG
                ).show()

                btnNext.isEnabled = true
            }
        }
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
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}