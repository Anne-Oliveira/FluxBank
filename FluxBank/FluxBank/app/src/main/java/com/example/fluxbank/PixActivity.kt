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
import com.example.fluxbank.network.ApiClient
import com.example.fluxbank.utils.TokenManager
import kotlinx.coroutines.launch

class PixActivity : BaseActivity() {

    private lateinit var tokenManager: TokenManager

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

                    val intent = Intent(this@PixActivity, DefinirValorPixActivity::class.java)
                    intent.putExtra("PIX_KEY", pixKey)
                    intent.putExtra("CONTA_ID", contaInfo.id)
                    intent.putExtra("NOME_DESTINATARIO", contaInfo.nomeUsuario)
                    intent.putExtra("DOCUMENTO_DESTINATARIO", contaInfo.getDocumento())
                    intent.putExtra("DOCUMENTO_MASCARADO", contaInfo.getDocumentoMascarado())
                    intent.putExtra("TIPO_DOCUMENTO", contaInfo.getTipoDocumento())
                    intent.putExtra("INSTITUICAO", contaInfo.instituicao)
                    startActivity(intent)

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
        val recentContactsList = findViewById<ListView>(R.id.recent_contacts)

        val contacts = listOf(
            Contact("Anne"),
            Contact("João"),
            Contact("Maria"),
            Contact("Pedro"),
            Contact("Carlos")
        )

        val adapter = RecentContactsAdapter(this, contacts)
        recentContactsList.adapter = adapter

        recentContactsList.post {
            ListViewHelper.setListViewHeightBasedOnChildren(recentContactsList)
        }
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
            val option = options[position]
            showToast("${option.title} clicado")
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
            val option = options[position]
            showToast("${option.title} clicado")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}