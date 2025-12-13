package com.example.fluxbank

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.example.fluxbank.network.ApiClient
import com.example.fluxbank.utils.TokenManager
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.JustifyContent
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ChatbotActivity : BaseActivity() {

    private var step = "inicio"
    private lateinit var tokenManager: TokenManager
    private lateinit var chatContainer: LinearLayout
    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        tokenManager = TokenManager(this)

        val edtMessage = findViewById<EditText>(R.id.edtMessage)
        val btnSend = findViewById<ImageButton>(R.id.btnSend)
        chatContainer = findViewById(R.id.chatContainer)
        scrollView = findViewById(R.id.chatScroll)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        addBotMessage(
            "Olá! Sou o Fluxy,\nseu assistente virtual 24h ⭐",
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

            processUserMessage(userMessage)

            edtMessage.text.clear()
            scrollToBottom()
        }
    }

    private fun processUserMessage(message: String) {
        val m = message.lowercase()

        if (step == "boleto_valor") {
            step = "inicio"
            val codigoBarras = gerarCodigoBarras(message)
            addBotMessage(
                "Boleto gerado com sucesso! 💳\n\n" +
                        "Valor: R$ $message\n" +
                        "Código de barras:\n$codigoBarras",
                chatContainer
            )
            scrollToBottom()
            return
        }

        when {
            m.contains("saldo") -> buscarSaldo()
            m.contains("extrato") || m.contains("transação") -> buscarExtrato()
            m.contains("minha conta") || m.contains("conta") -> buscarDadosConta()
            m.contains("cartão") || m.contains("cartões") -> buscarCartoes()
            m.contains("pix") -> mostrarInfoPix()
            m.contains("boleto") -> solicitarBoleto()
            m.contains("faq") || m.contains("ajuda") || m.contains("duvida") -> mostrarFAQ()
            m.contains("fatura") || m.contains("faturas") -> buscarFaturas()
            m.contains("cancelar") -> mostrarCancelamento()
            m.contains("opções") || m.contains("mais opções") -> mostrarMaisOpcoes()
            m.contains("oi") || m.contains("olá") || m.contains("ola") -> mostrarSaudacao()
            m.contains("tchau") || m.contains("até") || m.contains("ate") -> mostrarDespedida()
            else -> mostrarNaoEntendido()
        }
    }

    private fun buscarSaldo() {
        addBotMessage("Consultando seu saldo... ⏳", chatContainer)
        scrollToBottom()

        lifecycleScope.launch {
            try {
                val token = tokenManager.getToken()
                val contaId = tokenManager.getContaId()

                if (token == null || contaId == 0L) {
                    addBotMessage("Erro: Sessão inválida. Faça login novamente.", chatContainer)
                    scrollToBottom()
                    return@launch
                }

                val response = ApiClient.api.buscarExtrato(contaId, "Bearer $token")

                if (response.isSuccessful && response.body() != null) {
                    val extrato = response.body()!!
                    val saldoFormatado = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))
                        .format(extrato.saldoAtual)

                    chatContainer.removeViewAt(chatContainer.childCount - 1)

                    addBotMessage(
                        "Seu saldo atual é:\n💰 $saldoFormatado",
                        chatContainer
                    )
                } else {
                    chatContainer.removeViewAt(chatContainer.childCount - 1)
                    addBotMessage("Erro ao consultar saldo. Tente novamente.", chatContainer)
                }
            } catch (e: Exception) {
                Log.e("Chatbot", "Erro ao buscar saldo", e)
                chatContainer.removeViewAt(chatContainer.childCount - 1)
                addBotMessage("Erro ao consultar saldo. Verifique sua conexão.", chatContainer)
            }
            scrollToBottom()
        }
    }

    private fun buscarExtrato() {
        addBotMessage("Buscando suas transações... 📋", chatContainer)
        scrollToBottom()

        lifecycleScope.launch {
            try {
                val token = tokenManager.getToken()
                val contaId = tokenManager.getContaId()

                if (token == null || contaId == 0L) {
                    chatContainer.removeViewAt(chatContainer.childCount - 1)
                    addBotMessage("Erro: Sessão inválida. Faça login novamente.", chatContainer)
                    scrollToBottom()
                    return@launch
                }

                val response = ApiClient.api.buscarExtrato(contaId, "Bearer $token")

                if (response.isSuccessful && response.body() != null) {
                    val extrato = response.body()!!
                    val transacoes = extrato.transacoes

                    chatContainer.removeViewAt(chatContainer.childCount - 1)

                    if (transacoes.isEmpty()) {
                        addBotMessage("Você ainda não tem transações registradas.", chatContainer)
                    } else {
                        val ultimasTransacoes = transacoes.take(5)
                        val extratoMsg = buildString {
                            append("Suas últimas transações:\n\n")
                            ultimasTransacoes.forEachIndexed { index, transacao ->
                                val valor = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))
                                    .format(transacao.valor)
                                val tipo = transacao.tipoTransacao
                                val sinal = if (transacao.ehEntrada == true) "+" else "−"
                                append("${index + 1}. $tipo $sinal $valor\n")
                                if (!transacao.descricao.isNullOrEmpty()) {
                                    append("   ${transacao.descricao}\n")
                                }
                            }
                        }
                        addBotMessage(extratoMsg.trim(), chatContainer)
                    }
                } else {
                    chatContainer.removeViewAt(chatContainer.childCount - 1)
                    addBotMessage("Erro ao buscar extrato. Tente novamente.", chatContainer)
                }
            } catch (e: Exception) {
                Log.e("Chatbot", "Erro ao buscar extrato", e)
                chatContainer.removeViewAt(chatContainer.childCount - 1)
                addBotMessage("Erro ao buscar extrato. Verifique sua conexão.", chatContainer)
            }
            scrollToBottom()
        }
    }

    private fun buscarDadosConta() {
        addBotMessage("Consultando dados da conta... 🏦", chatContainer)
        scrollToBottom()

        lifecycleScope.launch {
            try {
                val token = tokenManager.getToken()
                val contaId = tokenManager.getContaId()

                if (token == null || contaId == 0L) {
                    chatContainer.removeViewAt(chatContainer.childCount - 1)
                    addBotMessage("Erro: Sessão inválida. Faça login novamente.", chatContainer)
                    scrollToBottom()
                    return@launch
                }

                val response = ApiClient.api.buscarExtrato(contaId, "Bearer $token")

                if (response.isSuccessful && response.body() != null) {
                    val extrato = response.body()!!

                    chatContainer.removeViewAt(chatContainer.childCount - 1)

                    val info = buildString {
                        append("📌 Informações da Conta\n\n")
                        append("Agência: ${extrato.agencia}\n")
                        append("Conta: ${extrato.numeroConta}\n")
                        append("Saldo: ${NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR")).format(extrato.saldoAtual)}\n")
                        append("Total de transações: ${extrato.totalTransacoes}")
                    }

                    addBotMessage(info, chatContainer)
                } else {
                    chatContainer.removeViewAt(chatContainer.childCount - 1)
                    addBotMessage("Erro ao buscar dados da conta.", chatContainer)
                }
            } catch (e: Exception) {
                Log.e("Chatbot", "Erro ao buscar conta", e)
                chatContainer.removeViewAt(chatContainer.childCount - 1)
                addBotMessage("Erro ao buscar dados. Verifique sua conexão.", chatContainer)
            }
            scrollToBottom()
        }
    }

    private fun buscarCartoes() {
        addBotMessage("Consultando seus cartões... 💳", chatContainer)
        scrollToBottom()

        lifecycleScope.launch {
            try {
                val token = tokenManager.getToken()
                val contaId = tokenManager.getContaId()

                if (token == null || contaId == 0L) {
                    chatContainer.removeViewAt(chatContainer.childCount - 1)
                    addBotMessage("Erro: Sessão inválida. Faça login novamente.", chatContainer)
                    scrollToBottom()
                    return@launch
                }

                // Não existe buscarConta, então simula cartões a partir do contaId
                chatContainer.removeViewAt(chatContainer.childCount - 1)

                val info = buildString {
                    append("💳 Seus Cartões\n\n")
                    append("• Cartão de Débito\n")
                    append("  Final: ****${contaId.toString().takeLast(4)}\n")
                    append("  Status: Ativo ✅\n\n")
                    append("• Cartão de Crédito\n")
                    append("  Final: ****${(contaId + 1000).toString().takeLast(4)}\n")
                    append("  Status: Ativo ✅")
                }

                addBotMessage(info, chatContainer)
            } catch (e: Exception) {
                Log.e("Chatbot", "Erro ao buscar cartões", e)
                chatContainer.removeViewAt(chatContainer.childCount - 1)
                addBotMessage("Erro ao buscar cartões. Verifique sua conexão.", chatContainer)
            }
            scrollToBottom()
        }
    }

    private fun buscarFaturas() {
        addBotMessage("Consultando faturas... 📄", chatContainer)
        scrollToBottom()

        lifecycleScope.launch {
            try {
                val token = tokenManager.getToken()
                val contaId = tokenManager.getContaId()

                if (token == null || contaId == 0L) {
                    chatContainer.removeViewAt(chatContainer.childCount - 1)
                    addBotMessage("Erro: Sessão inválida. Faça login novamente.", chatContainer)
                    scrollToBottom()
                    return@launch
                }

                chatContainer.removeViewAt(chatContainer.childCount - 1)

                val meses = listOf("Janeiro", "Fevereiro", "Março")
                val valores = listOf(320.00, 198.00, 440.00)

                val info = buildString {
                    append("📄 Faturas do Cartão\n\n")
                    meses.forEachIndexed { index, mes ->
                        val valorFormatado = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))
                            .format(valores[index])
                        append("• $mes: $valorFormatado\n")
                    }
                }

                addBotMessage(info.trim(), chatContainer)
            } catch (e: Exception) {
                Log.e("Chatbot", "Erro ao buscar faturas", e)
                chatContainer.removeViewAt(chatContainer.childCount - 1)
                addBotMessage("Erro ao buscar faturas. Verifique sua conexão.", chatContainer)
            }
            scrollToBottom()
        }
    }

    private fun mostrarInfoPix() {
        val info = buildString {
            append("💸 Informações sobre PIX\n\n")
            append("Para realizar um PIX:\n")
            append("1. Acesse a área 'Transferências'\n")
            append("2. Escolha o tipo de chave\n")
            append("3. Informe os dados do destinatário\n")
            append("4. Confirme o valor e finalize\n\n")
            append("Você também pode usar o QR Code!")
        }
        addBotMessage(info, chatContainer)
        scrollToBottom()
    }

    private fun solicitarBoleto() {
        step = "boleto_valor"
        addBotMessage("Claro! Qual o valor do boleto que você deseja gerar?", chatContainer)
        scrollToBottom()
    }

    private fun mostrarFAQ() {
        val faq = buildString {
            append("❓ Perguntas Frequentes\n\n")
            append("• Como abrir conta?\n")
            append("  Clique em 'Criar conta' na tela inicial.\n\n")
            append("• Horário de atendimento?\n")
            append("  Segunda a sexta, 08h às 18h.\n\n")
            append("• O FluxBank é seguro?\n")
            append("  Sim! Utilizamos criptografia de ponta\n")
            append("  e seguimos todas as normas do Banco Central.")
        }
        addBotMessage(faq, chatContainer)
        scrollToBottom()
    }

    private fun mostrarCancelamento() {
        val aviso = buildString {
            append("⚠️ Cancelamento de Conta\n\n")
            append("Tem certeza que deseja cancelar sua conta?\n")
            append("Essa ação é irreversível!\n\n")
            append("Para prosseguir com o cancelamento,\n")
            append("entre em contato com nosso suporte:\n")
            append("📞 0800-123-4567")
        }
        addBotMessage(aviso, chatContainer)
        scrollToBottom()
    }

    private fun mostrarMaisOpcoes() {
        val opcoes = buildString {
            append("📋 Mais Opções\n\n")
            append("• Cartões\n")
            append("• Faturas\n")
            append("• PIX\n")
            append("• Extrato\n")
            append("• Minha conta\n")
            append("• Saldo\n")
            append("• Cancelar conta")
        }
        addBotMessage(opcoes, chatContainer)
        scrollToBottom()
    }

    private fun mostrarSaudacao() {
        val saudacoes = listOf(
            "Olá! Como posso ajudar? 😊",
            "Oi! Em que posso ser útil?",
            "Olá! Estou aqui para te ajudar!"
        )
        addBotMessage(saudacoes.random(), chatContainer)
        scrollToBottom()
    }

    private fun mostrarDespedida() {
        val despedidas = listOf(
            "Até mais! Estou aqui quando precisar! 👋",
            "Tchau! Volte sempre! 😊",
            "Até logo! Foi um prazer ajudar! ✨"
        )
        addBotMessage(despedidas.random(), chatContainer)
        scrollToBottom()
    }

    private fun mostrarNaoEntendido() {
        val mensagem = buildString {
            append("Não entendi sua solicitação 🤔\n\n")
            append("Você pode tentar:\n")
            append("• Saldo\n")
            append("• Extrato\n")
            append("• Minha conta\n")
            append("• Cartões\n")
            append("• PIX\n")
            append("• FAQ")
        }
        addBotMessage(mensagem, chatContainer)
        scrollToBottom()
    }

    private fun gerarCodigoBarras(valor: String): String {
        val random = Random()
        val banco = "341"
        val moeda = "9"
        val digito = random.nextInt(10)

        val parte1 = String.format("%5d.%5d", random.nextInt(100000), random.nextInt(100000))
        val parte2 = String.format("%5d.%6d", random.nextInt(100000), random.nextInt(1000000))
        val parte3 = String.format("%5d.%6d", random.nextInt(100000), random.nextInt(1000000))
        val parte4 = digito
        val parte5 = String.format("%14d", random.nextInt(99999999))

        return "$parte1 $parte2 $parte3 $parte4 $parte5"
    }

    private fun scrollToBottom() {
        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun addBotMessage(text: String, container: LinearLayout) {
        val view = layoutInflater.inflate(R.layout.item_bot_message, null)
        val tv = view.findViewById<TextView>(R.id.textBot)
        tv.text = text
        container.addView(view)
    }

    private fun addUserMessage(text: String, container: LinearLayout) {
        val view = layoutInflater.inflate(R.layout.item_user_message, null)
        val tv = view.findViewById<TextView>(R.id.textUser)
        tv.text = text
        container.addView(view)
    }

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
                processUserMessage(opt.lowercase())
            }

            flex.addView(item)
        }

        container.addView(flex)
    }
}