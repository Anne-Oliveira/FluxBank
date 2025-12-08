package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class FaqActivity : AppCompatActivity() {

    private lateinit var recyclerViewFaq: RecyclerView
    private lateinit var campoPesquisa: EditText
    private lateinit var btnEnviarPergunta: MaterialButton
    private lateinit var btnVoltar: ImageView

    private lateinit var adapter: FaqAdapter
    private lateinit var listaFaqCompleta: List<FaqItem>
    private lateinit var listaFaqFiltrada: MutableList<FaqItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)

        // Inicializar views
        recyclerViewFaq = findViewById(R.id.recyclerViewFaq)
        campoPesquisa = findViewById(R.id.campoPesquisa)
        btnEnviarPergunta = findViewById(R.id.btnEnviarPergunta)
        btnVoltar = findViewById(R.id.btn_back)

        // Configurar RecyclerView
        recyclerViewFaq.layoutManager = LinearLayoutManager(this)

        // Criar lista de perguntas
        criarListaFaq()

        // Configurar adapter
        listaFaqFiltrada = listaFaqCompleta.toMutableList()
        adapter = FaqAdapter(listaFaqFiltrada)
        recyclerViewFaq.adapter = adapter

        // Configurar pesquisa
        configurarPesquisa()

        // Configurar botões
        btnVoltar.setOnClickListener {
            finish()
        }

        btnEnviarPergunta.setOnClickListener {
            val intent = Intent(this, EnviarFaqActivity::class.java)
            startActivity(intent)
        }

        // Configurar bottom navigation
        setupBottomNavigation()
    }

    private fun criarListaFaq() {
        listaFaqCompleta = listOf(
            FaqItem(
                pergunta = "Como abrir uma conta no banco digital?",
                resposta = "Para abrir uma conta, baixe o aplicativo, preencha seus dados pessoais, envie uma foto do documento e pronto! Sua conta será criada em minutos."
            ),
            FaqItem(
                pergunta = "É seguro usar o banco digital?",
                resposta = "Sim! Utilizamos criptografia de ponta a ponta, autenticação de dois fatores e todas as transações são protegidas. Seus dados estão seguros conosco."
            ),
            FaqItem(
                pergunta = "Como fazer transferências (TED/DOC/Pix)?",
                resposta = "Acesse a tela inicial, clique em 'Pix' ou 'Transferir', escolha o destinatário, digite o valor e confirme com sua senha. É rápido e seguro!"
            ),
            FaqItem(
                pergunta = "Esqueci a senha. O que fazer?",
                resposta = "Na tela de login, clique em 'Esqueci minha senha', informe seu CPF/CNPJ e email. Enviaremos um link para redefinir sua senha."
            ),
            FaqItem(
                pergunta = "Como solicitar um cartão?",
                resposta = "Vá em 'Cartões' no menu principal, clique em 'Solicitar novo cartão', escolha o tipo (débito, crédito ou virtual) e aguarde a aprovação. Cartões virtuais são criados na hora!"
            ),
            FaqItem(
                pergunta = "Qual o limite de transferência por Pix?",
                resposta = "O limite padrão para Pix é de R$ 5.000,00 durante o dia e R$ 1.000,00 à noite (20h às 6h). Você pode ajustar esses limites nas configurações."
            ),
            FaqItem(
                pergunta = "Como funciona a poupança?",
                resposta = "Nossa poupança rende 0,5% ao mês automaticamente. Basta transferir dinheiro para a poupança e o rendimento será calculado diariamente."
            ),
            FaqItem(
                pergunta = "O que é o Cofrinho?",
                resposta = "O Cofrinho é uma forma de guardar dinheiro para objetivos específicos (viagem, emergência, etc). Você pode criar vários cofrinhos e cada um rende 0,3% ao mês."
            ),
            FaqItem(
                pergunta = "Como pagar boletos?",
                resposta = "Acesse 'Pagar' no menu, selecione 'Boleto', escaneie o código de barras ou digite a linha digitável, confira os dados e confirme o pagamento."
            ),
            FaqItem(
                pergunta = "Posso ter conta PF e PJ?",
                resposta = "Sim! Você pode ter uma conta pessoa física (CPF) e outra pessoa jurídica (CNPJ) no mesmo aplicativo. Basta criar ambas no cadastro."
            )
        )
    }

    private fun configurarPesquisa() {
        campoPesquisa.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarPerguntas(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filtrarPerguntas(texto: String) {
        listaFaqFiltrada.clear()

        if (texto.isEmpty()) {
            listaFaqFiltrada.addAll(listaFaqCompleta)
        } else {
            val textoBusca = texto.lowercase()
            listaFaqCompleta.forEach { faq ->
                if (faq.pergunta.lowercase().contains(textoBusca) ||
                    faq.resposta.lowercase().contains(textoBusca)) {
                    listaFaqFiltrada.add(faq)
                }
            }
        }

        adapter.notifyDataSetChanged()

        // Mostrar mensagem se não encontrar resultados
        if (listaFaqFiltrada.isEmpty()) {
            Toast.makeText(this, "Nenhuma pergunta encontrada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNavigation() {
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navList = findViewById<ImageView>(R.id.nav_list)
        val navQr = findViewById<ImageView>(R.id.nav_qr)
        val navTransfer = findViewById<ImageView>(R.id.nav_transfer)
        val navSettings = findViewById<ImageView>(R.id.nav_settings)

        navHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        navList.setOnClickListener {
            showToast("Lista clicado")
        }

        navQr.setOnClickListener {
            showToast("QR Code clicado")
        }

        navTransfer.setOnClickListener {
            showToast("Transferir clicado")
        }

        navSettings.setOnClickListener {
            showToast("Configurações clicado")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}