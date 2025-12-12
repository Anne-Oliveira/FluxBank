package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.example.fluxbank.network.ApiClient
import com.example.fluxbank.network.models.TransacaoResponse
import com.example.fluxbank.utils.TokenManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ExtratoActivity : BaseActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var saldoLabel: TextView
    private lateinit var saldoValue: TextView
    private lateinit var visibilityIcon: ImageView
    private lateinit var histInput: EditText
    private lateinit var listViewExtrato: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyView: TextView

    private var todasTransacoes: List<ExtratoItem> = emptyList()
    private var transacoesFiltradas: List<ExtratoItem> = emptyList()
    private var saldoReal: Double = 0.0
    private var isSaldoVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extrato)

        tokenManager = TokenManager(this)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        saldoLabel = findViewById(R.id.saldoLabel)
        saldoValue = findViewById(R.id.saldoValue)
        visibilityIcon = findViewById(R.id.visibilityIcon)
        histInput = findViewById(R.id.hist_input)
        listViewExtrato = findViewById(R.id.listViewExtrato)

        progressBar = findViewById(R.id.progressBar) ?: ProgressBar(this)
        emptyView = findViewById(R.id.emptyView) ?: TextView(this)

        btnBack.setOnClickListener { finish() }

        visibilityIcon.setOnClickListener {
            isSaldoVisible = !isSaldoVisible
            atualizarSaldo()
        }

        histInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filtrarTransacoes(s?.toString() ?: "")
            }
        })

        carregarExtrato()

        setupBottomNavigation()
    }

    private fun carregarExtrato() {
        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                listViewExtrato.visibility = View.GONE
                emptyView.visibility = View.GONE

                val token = tokenManager.getToken()
                val contaId = tokenManager.getContaId()

                Log.d("ExtratoActivity", "=== CARREGANDO EXTRATO ===")
                Log.d("ExtratoActivity", "Token exists: ${token != null}")
                Log.d("ExtratoActivity", "ContaId: $contaId")

                if (token == null || contaId == 0L) {
                    Log.e("ExtratoActivity", "Token ou ContaId inválido")
                    mostrarErro("Sessão inválida. Faça login novamente.")
                    return@launch
                }

                val response = ApiClient.api.buscarExtrato(contaId, "Bearer $token")

                Log.d("ExtratoActivity", "Status: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    val extrato = response.body()!!

                    Log.d("ExtratoActivity", "Extrato recebido!")
                    Log.d("ExtratoActivity", "Saldo: ${extrato.saldoAtual}")
                    Log.d("ExtratoActivity", "Total transações: ${extrato.totalTransacoes}")
                    Log.d("ExtratoActivity", "Transações: ${extrato.transacoes.size}")

                    saldoReal = extrato.saldoAtual
                    atualizarSaldo()

                    todasTransacoes = converterTransacoes(extrato.transacoes)
                    transacoesFiltradas = todasTransacoes

                    if (todasTransacoes.isEmpty()) {
                        progressBar.visibility = View.GONE
                        listViewExtrato.visibility = View.GONE
                        emptyView.visibility = View.VISIBLE
                        emptyView.text = "Nenhuma transação nos últimos 30 dias"
                    } else {
                        atualizarLista()
                        progressBar.visibility = View.GONE
                        listViewExtrato.visibility = View.VISIBLE
                        emptyView.visibility = View.GONE
                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ExtratoActivity", "Erro: $errorBody")
                    mostrarErro("Erro ao carregar extrato: ${response.code()}")
                }

            } catch (e: Exception) {
                Log.e("ExtratoActivity", "Exceção", e)
                mostrarErro("Erro: ${e.message}")
            }
        }
    }

    private fun converterTransacoes(transacoes: List<TransacaoResponse>): List<ExtratoItem> {
        val sdf = SimpleDateFormat("MMM dd", Locale("pt", "BR"))

        return transacoes.map { t ->
            Log.d("ExtratoActivity", "─────────────────────")
            Log.d("ExtratoActivity", "Convertendo transação ${t.id}")
            Log.d("ExtratoActivity", "Tipo: ${t.tipoTransacao}")
            Log.d("ExtratoActivity", "Valor: ${t.valor}")
            Log.d("ExtratoActivity", "Entrada: ${t.ehEntrada}")

            val data = try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val date = inputFormat.parse(t.criadaEm)
                sdf.format(date ?: Date()).uppercase()
            } catch (e: Exception) {
                Log.e("ExtratoActivity", "Erro ao parsear data: ${t.criadaEm}", e)
                "---"
            }

            val (titulo, descricao) = when (t.tipoTransacao) {
                "PIX" -> {
                    if (t.ehEntrada == true) {
                        Pair("Pix recebido", t.nomeDestinatario ?: "Recebido")
                    } else {
                        Pair("Pix enviado", t.nomeDestinatario ?: "Enviado")
                    }
                }

                "TRANSFERENCIA" -> {
                    if (t.ehEntrada == true) {
                        Pair("Transferência recebida", t.nomeDestinatario ?: "Transferência")
                    } else {
                        Pair("Transferência enviada", t.nomeDestinatario ?: "Transferência")
                    }
                }

                "COFRINHO" -> {
                    val desc = t.descricao ?: "Cofrinho"
                    if (t.ehEntrada == true) {
                        Pair("Resgate", desc)
                    } else {
                        Pair("Depósito", desc)
                    }
                }

                "PAGAMENTO" -> Pair("Pagamento", t.nomeDestinatario ?: "Pagamento efetuado")
                "DEPOSITO" -> Pair("Depósito", t.nomeDestinatario ?: "Depósito realizado")
                "SAQUE" -> Pair("Saque", t.nomeDestinatario ?: "Saque efetuado")

                else -> {
                    if (t.ehEntrada == true) {
                        Pair("Entrada", t.descricao ?: "Entrada em conta")
                    } else {
                        Pair("Saída", t.descricao ?: "Saída de conta")
                    }
                }
            }

            val sinal = if (t.ehEntrada == true) "+" else "-"
            val valorFormatado = "$sinal R$ ${String.format("%.2f", t.valor).replace(".", ",")}"

            val icone = when (t.tipoTransacao) {
                "PIX" -> R.drawable.ic_pix
                "COFRINHO" -> R.drawable.ic_porquinho
                "TRANSFERENCIA" -> R.drawable.ic_transfer
                "PAGAMENTO" -> R.drawable.ic_pay
                "DEPOSITO" -> R.drawable.ic_investments
                "SAQUE" -> R.drawable.ic_cards
                else -> R.drawable.ic_list
            }

            ExtratoItem(data, titulo, descricao, valorFormatado, icone)
        }
    }

    private fun filtrarTransacoes(query: String) {
        transacoesFiltradas = if (query.isEmpty()) {
            todasTransacoes
        } else {
            val queryLower = query.lowercase()
            todasTransacoes.filter { item ->
                item.titulo.lowercase().contains(queryLower) ||
                        item.descricao.lowercase().contains(queryLower) ||
                        item.valor.lowercase().contains(queryLower) ||
                        item.data.lowercase().contains(queryLower)
            }
        }

        atualizarLista()

        if (transacoesFiltradas.isEmpty() && query.isNotEmpty()) {
            emptyView.text = "Nenhuma transação encontrada para \"$query\""
            emptyView.visibility = View.VISIBLE
            listViewExtrato.visibility = View.GONE
        } else if (transacoesFiltradas.isNotEmpty()) {
            emptyView.visibility = View.GONE
            listViewExtrato.visibility = View.VISIBLE
        }
    }

    private fun atualizarLista() {
        val adapter = ExtratoAdapter(this, transacoesFiltradas)
        listViewExtrato.adapter = adapter
    }

    private fun atualizarSaldo() {
        if (isSaldoVisible) {
            val saldoFormatado = String.format("R$ %.2f", saldoReal).replace(".", ",")
            saldoValue.text = saldoFormatado
            visibilityIcon.setImageResource(R.drawable.ic_visibility_off)
        } else {
            saldoValue.text = "R$ ********"
            visibilityIcon.setImageResource(R.drawable.ic_visibility)
        }
    }

    private fun mostrarErro(mensagem: String) {
        progressBar.visibility = View.GONE
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show()

        emptyView.text = mensagem
        emptyView.visibility = View.VISIBLE
        listViewExtrato.visibility = View.GONE
    }
}