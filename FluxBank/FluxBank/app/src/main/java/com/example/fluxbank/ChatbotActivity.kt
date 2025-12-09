package com.example.fluxbank

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.JustifyContent

class ChatbotActivity : BaseActivity() {

    private var step = "inicio"   // controla o fluxo do chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        val edtMessage = findViewById<EditText>(R.id.edtMessage)
        val btnSend = findViewById<ImageButton>(R.id.btnSend)
        val chatContainer = findViewById<LinearLayout>(R.id.chatContainer)
        val scrollView = findViewById<ScrollView>(R.id.chatScroll)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        // MENSAGENS INICIAIS
        addBotMessage(
            "Olá usuário! Sou o Fluxy,\nseu assistente virtual 24h ⭐",
            chatContainer
        )

        addBotMessage(
            "Escolha uma das opções abaixo\nou digite sua dúvida:",
            chatContainer
        )

        addQuickOptions(chatContainer)

        btnSend.setOnClickListener {
            val userMessage = edtMessage.text.toString().trim()
            if (userMessage.isEmpty()) return@setOnClickListener

            addUserMessage(userMessage, chatContainer)
            val botResponse = getResponse(userMessage)

            addBotMessage(botResponse, chatContainer)

            edtMessage.text.clear()

            scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
        }
    }

    // -------------------------------------------------------------
    // BALÃO DO BOT
    // -------------------------------------------------------------
    private fun addBotMessage(text: String, container: LinearLayout) {
        val view = layoutInflater.inflate(R.layout.item_bot_message, null)
        val tv = view.findViewById<TextView>(R.id.textBot)
        tv.text = text
        container.addView(view)
    }

    // -------------------------------------------------------------
    // BALÃO DO USUÁRIO
    // -------------------------------------------------------------
    private fun addUserMessage(text: String, container: LinearLayout) {
        val view = layoutInflater.inflate(R.layout.item_user_message, null)
        val tv = view.findViewById<TextView>(R.id.textUser)
        tv.text = text
        container.addView(view)
    }

    // -------------------------------------------------------------
    // BOTÕES ESTILO “PILL”
    // -------------------------------------------------------------
    private fun addQuickOptions(container: LinearLayout) {

        val options = listOf(
            "Minha conta", "Extrato", "Saldo",
            "FAQ", "Cartões", "Pix",
            "Cancelar conta", "Faturas", "Mais opções"
        )

        val flex = FlexboxLayout(this).apply {
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }

        for (opt in options) {
            val item = layoutInflater.inflate(R.layout.item_quick_option, null)
            val tv = item.findViewById<TextView>(R.id.optionText)
            tv.text = opt

            tv.setOnClickListener {
                addUserMessage(opt, container)
                val response = getResponse(opt.lowercase())
                addBotMessage(response, container)
            }

            flex.addView(item)
        }

        container.addView(flex)
    }


    // -------------------------------------------------------------
    // LÓGICA DO CHATBOT (a mesma que você já tinha)
    // -------------------------------------------------------------
    private fun getResponse(msg: String): String {
        val m = msg.lowercase()

        // --- Fluxo de boleto ---
        if (step == "boleto_valor") {
            step = "inicio"
            return "Boleto gerado com sucesso! 💳\nValor: R$ $msg\nCódigo: 34191.75839 48293.019584 91020.190001 2 93820000000000"
        }

        // ---- FLUXO PRINCIPAL ----
        return when {

            m.contains("saldo") -> {
                step = "inicio"
                "Seu saldo atual é **R$ 1.280,45** 💰"
            }

            m.contains("extrato") || m.contains("transação") -> {
                step = "inicio"
                "Aqui estão suas últimas movimentações:\n\n" +
                        "• PIX enviado − R$ 20,00\n" +
                        "• PIX recebido + R$ 150,00\n" +
                        "• Compra Mercado Livre − R$ 59,90\n" +
                        "• Recarga de celular − R$ 20,00"
            }

            m.contains("boleto") -> {
                step = "boleto_valor"
                "Claro! Qual o valor do boleto que você deseja gerar?"
            }

            m.contains("faq") || m.contains("ajuda") || m.contains("duvida") -> {
                step = "inicio"
                "Algumas dúvidas comuns:\n\n" +
                        "• Como abrir conta? — Clique em 'Criar conta'.\n" +
                        "• Horário? — 08h às 18h.\n" +
                        "• O FluxBank é seguro? — Sim! Utilizamos criptografia de ponta."
            }

            m.contains("cartão") || m.contains("cartões") -> {
                step = "inicio"
                "Você tem 2 cartões ativos:\n• Crédito final 2211\n• Débito final 8820"
            }

            m.contains("pix") -> {
                step = "inicio"
                "Para realizar um PIX, acesse a área 'Transferências' no app."
            }

            m.contains("minha conta") -> {
                step = "inicio"
                "Sua conta está ativa ✔\nTitular: Usuário FluxBank\nAgência: 0001\nConta: 123456-7"
            }

            m.contains("fatura") || m.contains("faturas") -> {
                step = "inicio"
                "Suas faturas:\n• Janeiro: R$ 320,00\n• Fevereiro: R$ 198,00\n• Março: R$ 440,00"
            }

            m.contains("cancelar") -> {
                step = "inicio"
                "Tem certeza que deseja cancelar a conta? Essa ação é irreversível."
            }

            m.contains("opções") || m.contains("mais opções") -> {
                step = "inicio"
                "Mais opções:\n• Cartões\n• Faturas\n• Pix\n• Cancelar conta"
            }

            m.contains("oi") || m.contains("olá") -> {
                step = "inicio"
                "Olá! Como posso ajudar?"
            }

            m.contains("tchau") || m.contains("até") -> {
                step = "inicio"
                "Até mais! 👋"
            }

            else -> {
                step = "inicio"
                "Não entendi 🤔\nVocê pode tentar:\nSaldo, Extrato, Boleto, FAQ..."
            }
        }
    }
}
