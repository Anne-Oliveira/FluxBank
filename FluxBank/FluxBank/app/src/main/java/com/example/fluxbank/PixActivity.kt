package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fluxbank.network.ApiClient
import com.example.fluxbank.network.models.ContatoRecente
import com.example.fluxbank.utils.TokenManager
import kotlinx.coroutines.launch

class PixActivity : BaseActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var contatosAdapter: RecentContactsAdapter
    private val contatosRecentes = mutableListOf<ContatoRecente>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val documento = intent?.getStringExtra("documento")
        val isCNPJ = documento?.length == 14
        val isCPF = documento?.length == 11

        when {
            isCNPJ -> setTheme(R.style.ThemeOverlay_FluxBank_CNPJ)
            isCPF -> setTheme(R.style.ThemeOverlay_FluxBank_CPF)
            else -> setTheme(R.style.ThemeOverlay_FluxBank_CPF)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pix)

        tokenManager = TokenManager(this)

        setupHeader()
        setupSearch()
        setupRecentContacts()
        setupOptionsGrid()
        setupManagementOptions()
        setupBottomNavigation()

        carregarContatosRecentes()
    }

    private fun setupHeader() {
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnHelp = findViewById<ImageView>(R.id.btn_help)

        btnBack.setOnClickListener { finish() }
        btnHelp.setOnClickListener { showToast("Ajuda clicado") }
    }

    private fun setupSearch() {
        val searchInput = findViewById<EditText>(R.id.search_input)
        val btnProsseguir = findViewById<Button>(R.id.btn_prosseguir)

        btnProsseguir.setOnClickListener {
            val pixKey = searchInput.text.toString().trim()

            if (pixKey.isEmpty()) {
                searchInput.error = "Digite uma chave Pix"
                return@setOnClickListener
            }

            buscarContaEProsseguir(pixKey, btnProsseguir)
        }
    }

    private fun buscarContaEProsseguir(pixKey: String, btnProsseguir: Button) {
        btnProsseguir.isEnabled = false
        btnProsseguir.text = "Buscando..."

        lifecycleScope.launch {
            try {
                val token = tokenManager.getToken()

                if (token == null) {
                    showToast("Erro: Sessão inválida")
                    btnProsseguir.isEnabled = true
                    btnProsseguir.text = "Prosseguir"
                    return@launch
                }

                Log.d("PixActivity", "Buscando conta com chave: $pixKey")

                val response = ApiClient.api.buscarContaPorChavePix(pixKey, "Bearer $token")

                if (response.isSuccessful && response.body() != null) {
                    val contaInfo = response.body()!!

                    Log.d("PixActivity", "Conta encontrada: ${contaInfo.nomeUsuario}")

                    irParaDefinirValor(contaInfo.chavePix, contaInfo.nomeUsuario,
                        contaInfo.getDocumento(), contaInfo.getDocumentoMascarado(),
                        contaInfo.getTipoDocumento(), contaInfo.instituicao)

                    btnProsseguir.isEnabled = true
                    btnProsseguir.text = "Prosseguir"

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("PixActivity", "Erro: $errorBody")

                    val errorMessage = when {
                        errorBody?.contains("não encontrada") == true -> "Chave Pix não encontrada"
                        errorBody?.contains("não está ativa") == true -> "Conta não está ativa"
                        else -> "Erro ao buscar chave Pix"
                    }

                    showToast(errorMessage)
                    btnProsseguir.isEnabled = true
                    btnProsseguir.text = "Prosseguir"
                }

            } catch (e: Exception) {
                Log.e("PixActivity", "Exceção", e)
                showToast("Erro: ${e.message}")
                btnProsseguir.isEnabled = true
                btnProsseguir.text = "Prosseguir"
            }
        }
    }

    private fun setupRecentContacts() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerContatosRecentes)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        contatosAdapter = RecentContactsAdapter(contatosRecentes) { contato ->
            // Redireciona para DefinirValorPixActivity com os dados do contato
            irParaDefinirValor(
                contato.chavePix,
                contato.nome ?: "Destinatário",
                contato.documento ?: "",
                contato.documentoMascarado ?: "***.**.***: ***-**",
                contato.tipoDocumento ?: "CPF",
                contato.instituicao
            )
        }

        recyclerView.adapter = contatosAdapter
    }

    private fun carregarContatosRecentes() {
        lifecycleScope.launch {
            try {
                val token = tokenManager.getToken()
                if (token == null) {
                    Log.e("PixActivity", "Token é nulo")
                    return@launch
                }

                Log.d("PixActivity", "=== BUSCANDO CONTATOS RECENTES ===")
                Log.d("PixActivity", "Token: ${token.take(20)}...")

                val response = ApiClient.api.buscarContatosRecentes("Bearer $token")

                Log.d("PixActivity", "Response code: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    val contatos = response.body()!!

                    Log.d("PixActivity", "Total de contatos recebidos: ${contatos.size}")

                    contatos.forEachIndexed { index, contato ->
                        Log.d("PixActivity", "=== CONTATO $index ===")
                        Log.d("PixActivity", "Nome: ${contato.nome}")
                        Log.d("PixActivity", "Nome Exibível: ${contato.getNomeExibivel()}")
                        Log.d("PixActivity", "Chave PIX: ${contato.chavePix}")
                        Log.d("PixActivity", "CPF: ${contato.cpf}")
                        Log.d("PixActivity", "CNPJ: ${contato.cnpj}")
                        Log.d("PixActivity", "Instituição: ${contato.instituicao}")
                    }

                    val contatosUnicos = contatos.distinctBy { it.chavePix }

                    Log.d("PixActivity", "Contatos únicos: ${contatosUnicos.size}")

                    contatosRecentes.clear()
                    contatosRecentes.addAll(contatosUnicos.take(10))
                    contatosAdapter.notifyDataSetChanged()

                    Log.d("PixActivity", "✅ ${contatosRecentes.size} contatos carregados no adapter")

                    contatosRecentes.forEachIndexed { index, contato ->
                        Log.d("PixActivity", "[$index] ${contato.getNomeExibivel()} - ${contato.chavePix}")
                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("PixActivity", "Erro ao carregar contatos recentes")
                    Log.e("PixActivity", "Error body: $errorBody")
                }

            } catch (e: Exception) {
                Log.e("PixActivity", "Exceção ao carregar contatos", e)
                Log.e("PixActivity", "Mensagem: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun irParaDefinirValor(
        pixKey: String,
        nome: String,
        documento: String,
        documentoMascarado: String,
        tipoDocumento: String,
        instituicao: String
    ) {
        val intent = Intent(this, DefinirValorPixActivity::class.java)
        intent.putExtra("PIX_KEY", pixKey)
        intent.putExtra("NOME_DESTINATARIO", nome)
        intent.putExtra("DOCUMENTO_DESTINATARIO", documento)
        intent.putExtra("DOCUMENTO_MASCARADO", documentoMascarado)
        intent.putExtra("TIPO_DOCUMENTO", tipoDocumento)
        intent.putExtra("INSTITUICAO", instituicao)
        startActivity(intent)
    }

    private fun setupOptionsGrid() {
        val optionsGrid = findViewById<GridView>(R.id.options_grid)

        val options = listOf(
            PixOption(R.drawable.ic_calendar, "Agendar Pix"),
            PixOption(R.drawable.ic_copy, "Copia e cola"),
            PixOption(R.drawable.ic_qrcode, "Ler Qr")
        )

        val adapter = OptionsGridAdapter(this, options)
        optionsGrid.adapter = adapter

        optionsGrid.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> startActivity(Intent(this, AgendarPagamentoActivity::class.java)) // Agendar Pix
                1 -> showToast("Copia e cola clicado")
                2 -> startActivity(Intent(this, LeitorQrActivity::class.java)) // Ler Qr
            }
        }
    }

    private fun setupManagementOptions() {
        val managementList = findViewById<ListView>(R.id.management_options)

        val options = listOf(
            ManagementOption(R.drawable.ic_key, "Minhas chaves"),
            ManagementOption(R.drawable.ic_limit, "Meus limites de transferência")
        )

        val adapter = ManagementOptionsAdapter(this, options)
        managementList.adapter = adapter

        managementList.post {
            ListViewHelper.setListViewHeightBasedOnChildren(managementList)
        }

        managementList.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    startActivity(Intent(this, MinhasChavesActivity::class.java))
                }
                1 -> {
                    showToast("Meus limites clicado")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}