package com.example.fluxbank

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fluxbank.utils.TokenManager

class DadosClienteActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dados_cliente)

        tokenManager = TokenManager(this)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        carregarDadosUsuario()
        configurarCliques()
    }

    private fun carregarDadosUsuario() {
        try {
            val nome = tokenManager.getUserName() ?: "Nome não disponível"
            val email = tokenManager.getUserEmail() ?: "email@exemplo.com"
            val cpf = tokenManager.getUserCpf() ?: ""
            val cnpj = tokenManager.getUserCnpj()

            Log.d("DadosCliente", "=== DADOS DO USUÁRIO ===")
            Log.d("DadosCliente", "Nome: $nome")
            Log.d("DadosCliente", "Email: $email")
            Log.d("DadosCliente", "CPF: $cpf")
            Log.d("DadosCliente", "CNPJ: $cnpj")

            val itemNome = findViewById<LinearLayout>(R.id.item_nome)
            itemNome.findViewById<TextView>(R.id.txt_nome_valor).text = nome
            itemNome.findViewById<TextView>(R.id.txt_nome_label).text = "Nome e sobrenome"

            val itemCpf = findViewById<LinearLayout>(R.id.item_cpf)
            if (cnpj != null && cnpj.isNotEmpty()) {
                val cnpjFormatado = formatarCNPJ(cnpj)
                itemCpf.findViewById<TextView>(R.id.txt_cpf_valor).text = cnpjFormatado
                itemCpf.findViewById<TextView>(R.id.txt_cpf_label).text = "Número do CNPJ"
            } else {
                val cpfFormatado = formatarCPF(cpf)
                itemCpf.findViewById<TextView>(R.id.txt_cpf_valor).text = cpfFormatado
                itemCpf.findViewById<TextView>(R.id.txt_cpf_label).text = "Número do CPF"
            }

            val itemEmail = findViewById<LinearLayout>(R.id.item_email)
            itemEmail.findViewById<TextView>(R.id.txt_email_valor).text = email

            val itemEndereco = findViewById<LinearLayout>(R.id.item_endereco)
            itemEndereco.findViewById<TextView>(R.id.txt_endereco_valor).text =
                "Não cadastrado"
            itemEndereco.findViewById<TextView>(R.id.txt_endereco_label).text =
                "Endereço particular"

            val itemOcupacao = findViewById<LinearLayout>(R.id.item_ocupacao)
            itemOcupacao.findViewById<TextView>(R.id.txt_ocupacao_valor).text =
                "Não informado"
            itemOcupacao.findViewById<TextView>(R.id.txt_ocupacao_label).text =
                "Ocupação principal"

            val itemSenha = findViewById<LinearLayout>(R.id.item_senha)
            itemSenha.findViewById<TextView>(R.id.txt_senha_valor).text = "************"

        } catch (e: Exception) {
            Log.e("DadosCliente", "Erro ao carregar dados", e)
            Toast.makeText(this, "Erro ao carregar dados do usuário", Toast.LENGTH_SHORT).show()
        }
    }

    private fun configurarCliques() {
        findViewById<LinearLayout>(R.id.item_nome).setOnClickListener {
            Toast.makeText(this, "Editar nome (em desenvolvimento)", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.item_cpf).setOnClickListener {
            Toast.makeText(this, "CPF/CNPJ não pode ser alterado", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.item_endereco).setOnClickListener {
            Toast.makeText(this, "Editar endereço (em desenvolvimento)", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.item_ocupacao).setOnClickListener {
            Toast.makeText(this, "Editar ocupação (em desenvolvimento)", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.item_email).setOnClickListener {
            Toast.makeText(this, "Editar email (em desenvolvimento)", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.item_senha).setOnClickListener {
            Toast.makeText(this, "Alterar senha (em desenvolvimento)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun formatarCPF(cpf: String): String {
        if (cpf.length != 11) return cpf

        return try {
            "${cpf.substring(0, 3)}.${cpf.substring(3, 6)}.${cpf.substring(6, 9)}-${cpf.substring(9, 11)}"
        } catch (e: Exception) {
            cpf
        }
    }

    private fun formatarCNPJ(cnpj: String): String {
        if (cnpj.length != 14) return cnpj

        return try {
            "${cnpj.substring(0, 2)}.${cnpj.substring(2, 5)}.${cnpj.substring(5, 8)}/${cnpj.substring(8, 12)}-${cnpj.substring(12, 14)}"
        } catch (e: Exception) {
            cnpj
        }
    }
}