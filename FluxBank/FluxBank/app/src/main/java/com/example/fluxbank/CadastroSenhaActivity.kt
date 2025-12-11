package com.example.fluxbank

import android.content.Context
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
import androidx.lifecycle.lifecycleScope
import com.example.fluxbank.network.ApiClient
import com.example.fluxbank.network.models.CadastroRequest
import com.example.fluxbank.utils.TokenManager
import kotlinx.coroutines.launch

/**
 * CadastroSenhaActivity - Cadastro (Última etapa: Senha)
 * ATUALIZADO: Salva agência junto com outros dados
 */
class CadastroSenhaActivity : BaseActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var regra8: TextView
    private lateinit var regraMaiuscula: TextView
    private lateinit var regraEspecial: TextView
    private lateinit var regraCoincide: TextView
    private lateinit var btnFinish: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_senha)

        tokenManager = TokenManager(this)

        val btnClose = findViewById<ImageView>(R.id.btnClose)
        btnFinish = findViewById(R.id.btnFinishCadastro)
        val edtSenha = findViewById<EditText>(R.id.edtSenha)
        val edtConfirmarSenha = findViewById<EditText>(R.id.edtConfirmarSenha)

        regra8 = findViewById(R.id.regra8)
        regraMaiuscula = findViewById(R.id.regraMaiuscula)
        regraEspecial = findViewById(R.id.regraEspecial)
        regraCoincide = findViewById(R.id.regraCoincide)

        val nome = intent.getStringExtra("nome") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val cpf = intent.getStringExtra("cpf")
        val cnpj = intent.getStringExtra("cnpj")
        val tipoUsuario = intent.getStringExtra("tipoUsuario") ?: "PF"

        Log.d("CadastroSenha", "=== Dados recebidos ===")
        Log.d("CadastroSenha", "Nome: $nome")
        Log.d("CadastroSenha", "Email: $email")
        Log.d("CadastroSenha", "CPF: $cpf")
        Log.d("CadastroSenha", "CNPJ: $cnpj")
        Log.d("CadastroSenha", "Tipo: $tipoUsuario")

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
                fazerCadastro(nome, email, cpf, cnpj, senha, confirmarSenha, tipoUsuario)
            } else {
                Toast.makeText(this, "Corrija os erros antes de continuar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fazerCadastro(
        nome: String,
        email: String,
        cpf: String?,
        cnpj: String?,
        senha: String,
        confirmarSenha: String,
        tipoUsuario: String
    ) {
        btnFinish.isEnabled = false

        lifecycleScope.launch {
            try {
                Log.d("CadastroSenha", "=== INÍCIO CADASTRO API ===")

                val request = CadastroRequest(
                    nomeCompleto = nome,
                    cpf = cpf,
                    cnpj = cnpj,
                    email = email,
                    senha = senha,
                    confirmarSenha = confirmarSenha,
                    telefone = null,
                    dataNascimento = null,
                    tipoUsuario = tipoUsuario
                )

                Log.d("CadastroSenha", "Request: $request")
                Log.d("CadastroSenha", "Fazendo chamada à API...")

                val response = ApiClient.api.cadastrar(request)

                Log.d("CadastroSenha", "Status Code: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!

                    Log.d("CadastroSenha", "✅ Cadastro bem-sucedido!")
                    Log.d("CadastroSenha", "Token: ${authResponse.token.substring(0, 20)}...")
                    Log.d("CadastroSenha", "Usuário: ${authResponse.usuario.nome}")

                    tokenManager.saveToken(authResponse.token)

                    val primeiraConta = authResponse.usuario.contas?.firstOrNull()

                    if (primeiraConta != null) {
                        Log.d("CadastroSenha", "Conta.id: ${primeiraConta.id}")
                        Log.d("CadastroSenha", "Conta.numeroConta: ${primeiraConta.numeroConta}")
                        Log.d("CadastroSenha", "Conta.agencia: ${primeiraConta.agencia}")  // ← LOG ADICIONADO
                        Log.d("CadastroSenha", "Conta.saldo: ${primeiraConta.saldo}")
                    }

                    // ========================================
                    // MUDANÇA AQUI: Adicionado agencia
                    // ========================================
                    tokenManager.saveUserData(
                        userId = authResponse.usuario.id,
                        userName = authResponse.usuario.nome,
                        email = authResponse.usuario.email,
                        cpf = authResponse.usuario.cpf,
                        cnpj = authResponse.usuario.cnpj,
                        contaId = primeiraConta?.id,
                        numeroConta = primeiraConta?.numeroConta,
                        agencia = primeiraConta?.agencia,
                        saldo = primeiraConta?.saldo?.toString()
                    )

                    addPasswordToSet(senha)

                    Toast.makeText(
                        this@CadastroSenhaActivity,
                        "Cadastro realizado! Bem-vindo, ${authResponse.usuario.nome ?: nome}!",
                        Toast.LENGTH_LONG
                    ).show()

                    val intent = Intent(this@CadastroSenhaActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("CadastroSenha", "❌ Erro ${response.code()}: $errorBody")

                    val errorMessage = try {
                        errorBody?.let {
                            if (it.contains("\"message\"")) {
                                it.substringAfter("\"message\":\"")
                                    .substringBefore("\"")
                            } else if (it.contains("já cadastrado")) {
                                "CPF/CNPJ ou email já cadastrado"
                            } else {
                                "Erro ao cadastrar"
                            }
                        } ?: "Erro ao cadastrar"
                    } catch (e: Exception) {
                        "Erro ao cadastrar"
                    }

                    Toast.makeText(
                        this@CadastroSenhaActivity,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()

                    btnFinish.isEnabled = true
                }

            } catch (e: Exception) {
                Log.e("CadastroSenha", "❌ Exceção", e)

                val errorMessage = when {
                    e.message?.contains("Unable to resolve host") == true ->
                        "Erro de conexão: Verifique se o backend está rodando"
                    e.message?.contains("timeout") == true ->
                        "Erro: Tempo esgotado"
                    else ->
                        "Erro: ${e.message}"
                }

                Toast.makeText(
                    this@CadastroSenhaActivity,
                    errorMessage,
                    Toast.LENGTH_LONG
                ).show()

                btnFinish.isEnabled = true
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