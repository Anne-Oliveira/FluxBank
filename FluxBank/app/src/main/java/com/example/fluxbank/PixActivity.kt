package com.example.fluxbank

import android.os.Bundle
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PixActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pix)

        // Configuração do botão voltar no header
        setupHeader()

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

    private fun setupBottomNavigation() {
        // Os IDs são dos LinearLayouts, não dos ImageViews!
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navList = findViewById<ImageView>(R.id.nav_list)
        val navQr = findViewById<ImageView>(R.id.nav_qr)
        val navTransfer = findViewById<ImageView>(R.id.nav_transfer)
        val navSettings = findViewById<ImageView>(R.id.nav_settings)

        navHome.setOnClickListener {
            finish() // Volta para a home
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