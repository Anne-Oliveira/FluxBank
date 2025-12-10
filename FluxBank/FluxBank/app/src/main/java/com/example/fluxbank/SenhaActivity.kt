package com.example.fluxbank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.fluxbank.network.ApiClient
import com.example.fluxbank.network.models.LoginRequest
import com.example.fluxbank.utils.TokenManager
import kotlinx.coroutines.launch

/**
 * Activity de senha - Login (Parte 2: Senha)
 * Recebe o documento (CPF/CNPJ) da tela anterior
 * Faz chamada à API para login
 * Mantém funcionalidade do checkbox de biometria
 */
class SenhaActivity : BaseActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var edtSenha: EditText
    private lateinit var btnNext: ImageButton
    private lateinit var chkBiometria: CheckBox
    // private lateinit var progressBar: ProgressBar  // Opcional: adicione no layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senha)

        // Inicializar TokenManager
        tokenManager = TokenManager(this)

        // Recuperar documento da tela anterior
        val documento = intent.getStringExtra("documento")
        if (documento == null) {
            Toast.makeText(this, "Erro: documento não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Inicializar views
        edtSenha = findViewById(R.id.edtSenha)
        btnNext = findViewById(R.id.btnNext)
        chkBiometria = findViewById(R.id.chkBiometria)
        // progressBar = findViewById(R.id.progressBar)  // Se tiver no layout

        // Carrega a preferência de biometria para o checkbox (funcionalidade original)
        chkBiometria.isChecked = isBiometricPreferenceEnabled()

        btnNext.setOnClickListener {
            val senha = edtSenha.text.toString()

            if (senha.isEmpty()) {
                edtSenha.error = "Digite sua senha"
                return@setOnClickListener
            }

            // Fazer login na API
            fazerLogin(documento, senha)
        }
    }

    /**
     * Faz chamada à API para login
     * Usa coroutine para não bloquear a UI
     */
    private fun fazerLogin(documento: String, senha: String) {
        // Desabilitar botão durante requisição
        btnNext.isEnabled = false
        // progressBar.visibility = View.VISIBLE  // Se tiver ProgressBar
        // Lançar coroutine
        lifecycleScope.launch {
            try {
                Log.d("SenhaActivity", "=== INÍCIO LOGIN ===")
                Log.d("SenhaActivity", "Documento: $documento")
                // Criar request
                val request = LoginRequest(documento, senha)
                // Fazer chamada à API
                val response = ApiClient.api.login(request)
                // Verificar se foi sucesso
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    Log.d("SenhaActivity", "✅ Login bem-sucedido!")
                    Log.d("SenhaActivity", "Token: ${authResponse.token.substring(0, 20)}...")
                    Log.d("SenhaActivity", "Usuário: ${authResponse.usuario.nome}")
                    // Salvar token e dados do usuário
                    tokenManager.saveToken(authResponse.token)
                    val primeiraConta = authResponse.usuario.contas?.firstOrNull()
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
                    // Salvar preferência de biometria (funcionalidade original)
                    saveBiometricPreference(chkBiometria.isChecked)
                    // Mostrar sucesso
                    Toast.makeText(
                        this@SenhaActivity,
                        "Bem-vindo, ${authResponse.usuario.nome}!",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Navegar para LoadingActivity
                    navigateToLoadingScreen()
                } else {
                    // Erro na resposta
                    val errorBody = response.errorBody()?.string()
                    Log.e("SenhaActivity", "❌ Erro no login: $errorBody")
                    // Tentar extrair mensagem de erro do JSON
                    val errorMessage = try {
                        // Se o backend retornar JSON com "message"
                        errorBody?.let {
                            if (it.contains("\"message\"")) {
                                it.substringAfter("\"message\":\"")
                                    .substringBefore("\"")
                            } else {
                                "Usuário ou senha incorretos"
                            }
                        } ?: "Usuário ou senha incorretos"
                    } catch (e: Exception) {
                        "Usuário ou senha incorretos"
                    }
                    edtSenha.error = "Senha incorreta"
                    Toast.makeText(
                        this@SenhaActivity,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
                    // Reabilitar botão
                    btnNext.isEnabled = true
                }
            } catch (e: Exception) {
                // Erro de rede ou outro erro
                Log.e("SenhaActivity", "❌ Exceção no login", e)
                val errorMessage = when {
                    e.message?.contains("Unable to resolve host") == true ->
                        "Erro de conexão: Verifique se o backend está rodando"
                    e.message?.contains("timeout") == true ->
                        "Erro: Tempo esgotado. Verifique sua conexão"
                    else ->
                        "Erro de conexão: ${e.message}"
                }
                Toast.makeText(
                    this@SenhaActivity,
                    errorMessage,
                    Toast.LENGTH_LONG
                ).show()
                // Reabilitar botão
                btnNext.isEnabled = true
            } finally {
                // progressBar.visibility = View.GONE  // Se tiver ProgressBar
            }
        }
    }

    /**
     * Salva a preferência de biometria (funcionalidade original mantida)
     */
    private fun saveBiometricPreference(isEnabled: Boolean) {
        val prefs = getSharedPreferences("fluxbank_prefs", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putBoolean("biometric_enabled", isEnabled)
            apply()
        }
    }

    /**
     * Verifica se a preferência de biometria está habilitada (funcionalidade original mantida)
     */
    private fun isBiometricPreferenceEnabled(): Boolean {
        val prefs = getSharedPreferences("fluxbank_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("biometric_enabled", false)
    }

    /**
     * Navega para a próxima tela após login bem-sucedido
     */
    private fun navigateToLoadingScreen() {
        val intent = Intent(this, LoadingActivity::class.java)
        // Limpar pilha de activities (usuário não volta para login)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}