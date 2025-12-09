package com.example.fluxbank

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ChatbotActivity : AppCompatActivity() {

    private var step = "inicio"   // controla o fluxo do chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        val edtMessage = findViewById<EditText>(R.id.edtMessage)
        val btnSend = findViewById<ImageButton>(R.id.btnSend)
        val chatContainer = findViewById<LinearLayout>(R.id.chatContainer)
        val scrollView = findViewById<ScrollView>(R.id.chatScroll)
        val btnBack = findViewById<ImageView>(R.id.btnBack)


        btnBack.setOnClickListener {
            finish()
        }
        addMessage("Bot: Olá! 👋 Eu sou o assistente do FluxBank.\nComo posso ajudar hoje?\n\n• Saldo\n• Extrato\n• Boleto\n• Ajuda", chatContainer)

        btnSend.setOnClickListener {
            val userMessage = edtMessage.text.toString().trim()
            if (userMessage.isEmpty()) return@setOnClickListener


            addMessage("Você: $userMessage", chatContainer)
            val botResponse = getResponse(userMessage)

            addMessage("Bot: $botResponse", chatContainer)

            edtMessage.text.clear()
            scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
        }
    }



    private fun addMessage(text: String, container: LinearLayout) {
        val tv = TextView(this)
        tv.text = text
        tv.textSize = 17f
        tv.setPadding(12, 12, 12, 12)
        container.addView(tv)
    }

    private fun getResponse(msg: String): String {
        val m = msg.lowercase()

        // --- Fluxo de boleto ---
        if (step == "boleto_valor") {
            step = "inicio"
            return "Boleto gerado com sucesso! 💳\nValor: R$ $msg\nCódigo: 34191.75839 48293.019584 91020.190001 2 93820000000000"
        }

        // --- Fluxo principal ---
        return when {

            // SALDO
            m.contains("saldo") -> {
                step = "inicio"
                "Seu saldo atual é **R$ 1.280,45** 💰"
            }

            // EXTRATO
            m.contains("extrato") || m.contains("transação") -> {
                step = "inicio"
                "Aqui estão suas últimas movimentações:\n\n" +
                        "• PIX enviado − R$ 20,00\n" +
                        "• PIX recebido + R$ 150,00\n" +
                        "• Compra Mercado Livre − R$ 59,90\n" +
                        "• Recarga de celular − R$ 20,00"
            }

            // BOLETO
            m.contains("boleto") -> {
                step = "boleto_valor"
                "Claro! Qual o valor do boleto que você deseja gerar?"
            }

            // AJUDA / FAQ
            m.contains("ajuda") || m.contains("duvida") -> {
                step = "inicio"
                "Aqui estão algumas dúvidas comuns:\n\n" +
                        "• Como abrir conta? — Basta clicar em 'Criar conta' na tela inicial.\n" +
                        "• Horário de atendimento? — 08h às 18h.\n" +
                        "• O FluxBank é seguro? — Sim! Utilizamos criptografia de ponta."
            }

            // OI / OLÁ
            m.contains("oi") || m.contains("olá") -> {
                step = "inicio"
                "Olá! Como posso ajudar?\n\n• Saldo\n• Extrato\n• Boleto\n• Ajuda"
            }

            // DESPEDIDA
            m.contains("tchau") || m.contains("até") -> {
                step = "inicio"
                "Até mais! 👋"
            }

            // DESCONHECIDO
            else -> {
                step = "inicio"
                "Não consegui entender 🤔\nVocê pode tentar:\n• Saldo\n• Extrato\n• Boleto\n• Ajuda"
            }
        }
    }
}
