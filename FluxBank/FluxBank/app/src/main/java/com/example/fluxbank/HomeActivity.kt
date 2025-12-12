package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fluxbank.network.ApiClient
import com.example.fluxbank.network.models.TransacaoResponse
import com.example.fluxbank.utils.TokenManager
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity() {

    private lateinit var tokenManager: TokenManager
    private var isSaldoVisible = false
    private var saldoReal: Double = 0.0

    // Guarda o documento recebido via Intent para reutilizar ao iniciar outras Activities
    private var documento: String? = null

    private val limiteTotal = 1400.0
    private val limiteUsado = 924.0

    override fun onCreate(savedInstanceState: Bundle?) {

        // armazena o documento em campo de classe
        documento = intent.getStringExtra("documento")

        val isCNPJ = documento?.length == 14
        val isCPF = documento?.length == 11

        when {
            isCNPJ -> {
                setTheme(R.style.ThemeOverlay_FluxBank_CNPJ)
            }
            isCPF -> {
                setTheme(R.style.ThemeOverlay_FluxBank_CPF)
            }
            else -> {
                setTheme(R.style.ThemeOverlay_FluxBank_CPF)
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tokenManager = TokenManager(this)
        carregarDadosUsuario()
        setupVisibility()
        setupNavigation()
        setupCofinho()
        setupInvoiceModule()
        setupActionButtons()
        setupBottomNavigation()
    }

    private fun carregarDadosUsuario() {
        val userName = tokenManager.getUserName() ?: "Usuário"
        val numeroConta = tokenManager.getNumeroConta() ?: "0000000000"
        val agencia = tokenManager.getAgencia() ?: "0001"

        val saldoStr = tokenManager.getSaldo() ?: "0.00"

        try {
            saldoReal = saldoStr.toDouble()
        } catch (e: Exception) {
            saldoReal = 0.0
        }

        Log.d("HomeActivity", "=== DADOS DO USUÁRIO ===")
        Log.d("HomeActivity", "Nome: $userName")
        Log.d("HomeActivity", "Agência: $agencia")
        Log.d("HomeActivity", "Conta: $numeroConta")
        Log.d("HomeActivity", "Saldo inicial: R$ $saldoReal")

        findViewById<TextView>(R.id.userName).text = userName
        findViewById<TextView>(R.id.agencyLabel).text = "Ag $agencia"
        findViewById<TextView>(R.id.accountLabel).text = "Cc $numeroConta"

        buscarTransacoes()
    }

    private fun buscarTransacoes() {
        lifecycleScope.launch {
            try {
                val token = tokenManager.getToken()
                val contaId = tokenManager.getContaId()

                Log.d("HomeActivity", "════════════════════════════")
                Log.d("HomeActivity", "DEBUG TRANSAÇÕES")
                Log.d("HomeActivity", "════════════════════════════")
                Log.d("HomeActivity", "Token exists: ${token != null}")
                Log.d("HomeActivity", "ContaId: $contaId")

                if (token == null || contaId == 0L) {
                    Log.e("HomeActivity", "Token ou ContaId inválido")
                    setupRecyclerViewSemTransacoes()
                    return@launch
                }

                val authHeader = "Bearer $token"
                Log.d("HomeActivity", "URL: /api/extrato/conta/$contaId/ultimos-30-dias")

                val response = ApiClient.api.buscarExtrato(contaId, authHeader)

                Log.d("HomeActivity", "════════════════════════════")
                Log.d("HomeActivity", "RESPOSTA")
                Log.d("HomeActivity", "════════════════════════════")
                Log.d("HomeActivity", "Status: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    val extrato = response.body()!!

                    Log.d("HomeActivity", "Extrato recebido!")
                    Log.d("HomeActivity", "Conta: ${extrato.numeroConta}")
                    Log.d("HomeActivity", "Agência: ${extrato.agencia}")
                    Log.d("HomeActivity", "Saldo REAL: R$ ${extrato.saldoAtual}")
                    Log.d("HomeActivity", "Total de transações: ${extrato.totalTransacoes}")

                    saldoReal = extrato.saldoAtual

                    // Salvar saldo atualizado no TokenManager
                    tokenManager.saveSaldo(saldoReal.toString())

                    Log.d("HomeActivity", "Saldo atualizado: R$ $saldoReal")

                    // Atualizar UI do saldo (se estiver visível)
                    if (isSaldoVisible) {
                        val saldoFormatado = String.format("R$ %.2f", saldoReal).replace(".", ",")
                        findViewById<TextView>(R.id.saldoValue).text = saldoFormatado
                    }

                    val transacoes = extrato.transacoes

                    if (transacoes.isEmpty()) {
                      
                        Log.d("HomeActivity", "Sem transações")

                        setupRecyclerViewSemTransacoes()
                    } else {
                        setupRecyclerViewComTransacoes(transacoes)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("HomeActivity", "Erro: $errorBody")
                    setupRecyclerViewSemTransacoes()
                }

            } catch (e: Exception) {
                Log.e("HomeActivity", "════════════════════════════")
                Log.e("HomeActivity", "EXCEÇÃO")
                Log.e("HomeActivity", "════════════════════════════")
                Log.e("HomeActivity", "Tipo: ${e.javaClass.simpleName}")
                Log.e("HomeActivity", "Mensagem: ${e.message}")
                e.printStackTrace()
                setupRecyclerViewSemTransacoes()
            }
        }
    }

    private fun setupRecyclerViewSemTransacoes() {
        val recyclerView = findViewById<RecyclerView>(R.id.recentActivityList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val btnConferirExtrato = findViewById<TextView>(R.id.dividerRecentActivity)
        btnConferirExtrato.setOnClickListener {
            startActivity(Intent(this, ExtratoActivity::class.java))
        }

        val activities = listOf(
            RecentActivity(
                "Nenhuma transação",
                "Você ainda não realizou transações",
                ""
            )
        )

        recyclerView.adapter = RecentActivityAdapter(activities)
    }

    private fun setupRecyclerViewComTransacoes(transacoes: List<TransacaoResponse>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recentActivityList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val btnConferirExtrato = findViewById<TextView>(R.id.dividerRecentActivity)
        btnConferirExtrato.setOnClickListener {
            startActivity(Intent(this, ExtratoActivity::class.java))
        }

        val ultimas3 = transacoes.take(3)

        Log.d("HomeActivity", "Mostrando ${ultimas3.size} transações")

        val activities = ultimas3.map { t ->
            Log.d("HomeActivity", "─────────────────────")
            Log.d("HomeActivity", "ID: ${t.id}")
            Log.d("HomeActivity", "Tipo: ${t.tipoTransacao}")
            Log.d("HomeActivity", "Valor: R$ ${t.valor}")
            Log.d("HomeActivity", "Entrada: ${t.ehEntrada}")
            Log.d("HomeActivity", "Destinatário: ${t.nomeDestinatario}")

            val (nome, descricao) = when (t.tipoTransacao) {
                "PIX" -> {
                    if (t.ehEntrada == true) {
                        Pair(t.nomeDestinatario ?: "Recebido", "Pix recebido")
                    } else {
                        Pair(t.nomeDestinatario ?: "Enviado", "Pix enviado")
                    }
                }

                "TRANSFERENCIA" -> {
                    if (t.ehEntrada == true) {
                        Pair(t.nomeDestinatario ?: "Transferência", "Transferência recebida")
                    } else {
                        Pair(t.nomeDestinatario ?: "Transferência", "Transferência enviada")
                    }
                }

                "COFRINHO" -> {
                    val descCofrinho = t.descricao ?: "Cofrinho"
                    if (t.ehEntrada == true) {
                        Pair("Cofrinho", "Resgate de $descCofrinho")
                    } else {
                        Pair("Cofrinho", "Depósito em $descCofrinho")
                    }
                }

                "PAGAMENTO" -> Pair(t.nomeDestinatario ?: "Pagamento", "Pagamento efetuado")
                "DEPOSITO" -> Pair(t.nomeDestinatario ?: "Depósito", "Depósito realizado")
                "SAQUE" -> Pair(t.nomeDestinatario ?: "Saque", "Saque efetuado")

                else -> {
                    if (t.ehEntrada == true) {
                        Pair(t.nomeDestinatario ?: "Entrada", t.descricao ?: "Entrada em conta")
                    } else {
                        Pair(t.nomeDestinatario ?: "Saída", t.descricao ?: "Saída de conta")
                    }
                }
            }

            val valorFormatado = String.format("R$ %.2f", t.valor).replace(".", ",")

            RecentActivity(nome, descricao, valorFormatado)
        }

        recyclerView.adapter = RecentActivityAdapter(activities)
        Log.d("HomeActivity", "✅ RecyclerView configurado!")
    }

    private fun setupVisibility() {
        val saldoValue = findViewById<TextView>(R.id.saldoValue)
        val visibilityIcon = findViewById<ImageView>(R.id.visibilityIcon)

        saldoValue.text = "R$ ********"

        visibilityIcon.setOnClickListener {
            isSaldoVisible = !isSaldoVisible
            if (isSaldoVisible) {
                val saldoFormatado = String.format("R$ %.2f", saldoReal).replace(".", ",")
                saldoValue.text = saldoFormatado
                visibilityIcon.setImageResource(R.drawable.ic_visibility_off)
            } else {
                saldoValue.text = "R$ ********"
                visibilityIcon.setImageResource(R.drawable.ic_visibility)
            }
        }
    }

    private fun setupNavigation() {
        findViewById<TextView>(R.id.cofinhoLink).setOnClickListener {
            val intent = Intent(this, CofinhoActivity::class.java)
            // passa o documento atual para manter o tema/estado nas próximas Activities
            intent.putExtra("documento", documento)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.helpIcon).setOnClickListener {
            startActivity(Intent(this, FaqActivity::class.java))
        }
        findViewById<ImageView>(R.id.notificationIcon).setOnClickListener {
            startActivity(Intent(this, NotificacaoActivity::class.java))
        }
    }

    private fun setupCofinho() {
        val cofinhoRecyclerView = findViewById<RecyclerView>(R.id.cofinhoRecyclerView)
        cofinhoRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val cofinhoItems = listOf(
            CofinhoItem(R.drawable.ic_porquinho, "Dia a Dia", "R$ 0,00", "Rendeu R$ 0,00"),
            CofinhoItem(R.drawable.ic_travel_bag, "Viagem", "R$ 0,00", "Rendeu R$ 0,00")
        )

        cofinhoRecyclerView.adapter = CofinhoAdapter(cofinhoItems)
    }

    private fun setupInvoiceModule() {
        val progressBar = findViewById<View>(R.id.home_progress_bar)
        val limiteUtilizado = findViewById<TextView>(R.id.home_limite_utilizado)
        val limiteDisponivel = findViewById<TextView>(R.id.home_limite_disponivel)
        val btnConferirFaturas = findViewById<TextView>(R.id.btn_conferir_faturas)

        val disponivel = limiteTotal - limiteUsado

        limiteUtilizado.text = String.format("R$ %.2f", limiteUsado).replace(".", ",")
        limiteDisponivel.text = String.format("R$ %.2f", disponivel).replace(".", ",")

        val screenWidth = resources.displayMetrics.widthPixels - 64
        val progressWidth = ((limiteUsado / limiteTotal) * screenWidth).toInt()

        progressBar.layoutParams.width = progressWidth

        btnConferirFaturas.setOnClickListener {
            startActivity(Intent(this, InvoiceActivity::class.java))
        }
    }

    private fun setupActionButtons() {
        findViewById<MaterialCardView>(R.id.btn_pix).setOnClickListener {
            val intentPix = Intent(this, PixActivity::class.java)
            intentPix.putExtra("documento", documento)
            startActivity(intentPix)
        }

        findViewById<MaterialCardView>(R.id.btn_cards).setOnClickListener {
            val intent = Intent(this, CardsActivity::class.java)
            intent.putExtra("documento", documento)
            startActivity(intent)
        }
        findViewById<MaterialCardView>(R.id.investmentsCard).setOnClickListener {
            val intent = Intent(this, PoupancaActivity::class.java)
            intent.putExtra("documento", documento)
            startActivity(intent)
        }
        findViewById<MaterialCardView>(R.id.btn_pay).setOnClickListener {
            val intent = Intent(this, PagamentosActivity::class.java)
            intent.putExtra("documento", documento)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.chatIcon).setOnClickListener {
            val intent = Intent(this, ChatbotActivity::class.java)
            intent.putExtra("documento", documento)
            startActivity(intent)
        }
    }
}