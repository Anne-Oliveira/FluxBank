package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast

class PixActivity : BaseActivity() {
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

        // Configuração do botão voltar no header
        setupHeader()

        // Configurar o campo de busca e o botão prosseguir
        setupSearch()

        // Configurar listas e grids
        setupRecentContacts()
        setupOptionsGrid()
        setupManagementOptions()

        // Configuração da navegação inferior
        setupBottomNavigation()
    }

    private fun setupHeader() {
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnHelp = findViewById<ImageView>(R.id.btn_help)

        btnBack.setOnClickListener {
            finish() // Volta para a tela anterior
        }

        btnHelp.setOnClickListener {
            showToast("Ajuda clicado")
        }
    }

    private fun setupSearch() {
        val searchInput = findViewById<EditText>(R.id.search_input)
        val btnProsseguir = findViewById<Button>(R.id.btn_prosseguir)

        btnProsseguir.setOnClickListener {
            val pixKey = searchInput.text.toString()
            if (pixKey.isNotEmpty()) {
                val intent = Intent(this, DefinirValorPixActivity::class.java)
                intent.putExtra("PIX_KEY", pixKey)
                startActivity(intent)
            } else {
                searchInput.error = "Digite uma chave Pix"
            }
        }
    }

    private fun setupRecentContacts() {
        val recentContactsList = findViewById<ListView>(R.id.recent_contacts)

        // Lista de contatos recentes
        val contacts = listOf(
            Contact("Anne"),
            Contact("João"),
            Contact("Maria"),
            Contact("Pedro"),
            Contact("Carlos")
        )

        val adapter = RecentContactsAdapter(this, contacts)
        recentContactsList.adapter = adapter

        // Ajusta a altura do ListView para mostrar todos os itens
        recentContactsList.post {
            ListViewHelper.setListViewHeightBasedOnChildren(recentContactsList)
        }
    }

    private fun setupOptionsGrid() {
        val optionsGrid = findViewById<GridView>(R.id.options_grid)

        // Opções do grid
        val options = listOf(
            PixOption(R.drawable.ic_calendar, "Agendar Pix"),
            PixOption(R.drawable.ic_copy, "Copia e cola"),
            PixOption(R.drawable.ic_qrcode, "Ler Qr")
        )

        val adapter = OptionsGridAdapter(this, options)
        optionsGrid.adapter = adapter

        // Click nos itens do grid
        optionsGrid.setOnItemClickListener { _, _, position, _ ->
            val option = options[position]
            showToast("${option.title} clicado")
        }
    }

    private fun setupManagementOptions() {
        val managementList = findViewById<ListView>(R.id.management_options)

        // Opções de gerenciamento
        val options = listOf(
            ManagementOption(R.drawable.ic_key, "Minhas chaves"),
            ManagementOption(R.drawable.ic_limit, "Meus limites de transferência")
        )

        val adapter = ManagementOptionsAdapter(this, options)
        managementList.adapter = adapter

        // Ajusta a altura do ListView para mostrar todos os itens
        managementList.post {
            ListViewHelper.setListViewHeightBasedOnChildren(managementList)
        }

        // Click nos itens da lista
        managementList.setOnItemClickListener { _, _, position, _ ->
            val option = options[position]
            showToast("${option.title} clicado")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}