package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class HomeActivity : BaseActivity() {

    private var isSaldoVisible = false
    private val saldoOculto = "R$********"
    private val saldoVisivel = "R$ 12.345,67"

    private val limiteTotal = 1400.0
    private val limiteUsado = 924.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val saldoValue = findViewById<TextView>(R.id.saldoValue)
        val visibilityIcon = findViewById<ImageView>(R.id.visibilityIcon)
        val cofinhoLink = findViewById<TextView>(R.id.cofinhoLink)
        val helpIcon = findViewById<ImageView>(R.id.helpIcon)

        // Configuração da visibilidade do saldo
        visibilityIcon.setOnClickListener {
            isSaldoVisible = !isSaldoVisible
            if (isSaldoVisible) {
                saldoValue.text = saldoVisivel
                visibilityIcon.setImageResource(R.drawable.ic_visibility_off)
            } else {
                saldoValue.text = saldoOculto
                visibilityIcon.setImageResource(R.drawable.ic_visibility)
            }
        }

        // Navegação para a tela do cofrinho
        cofinhoLink.setOnClickListener {
            val intent = Intent(this, CofinhoActivity::class.java)
            startActivity(intent)
        }

        // Navegação para a tela de FAQ
        helpIcon.setOnClickListener {
            val intent = Intent(this, FaqActivity::class.java)
            startActivity(intent)
        }

        // Configuração das Listas
        setupRecyclerView()
        setupCofinho()

        // Configurar módulo de fatura
        setupInvoiceModule()

        // Configuração dos botões de ação
        setupActionButtons()

        // Configuração dos cliques da Barra de Navegação
        setupBottomNavigation()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recentActivityList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val btnConferirExtrato = findViewById<TextView>(R.id.dividerRecentActivity)


        btnConferirExtrato.setOnClickListener {
            val intent = Intent(this, ExtratoActivity::class.java)
            startActivity(intent)
        }

        val activities = listOf(
            RecentActivity("Ivan", "Pix Recebido", "R$2.000"),
            RecentActivity("Mimic Fornecimento", "Transferência", "R$2.000"),
            RecentActivity("Ivan", "Pix", "R$2.000")
        )

        val adapter = RecentActivityAdapter(activities)
        recyclerView.adapter = adapter
    }

    private fun setupCofinho() {
        val cofinhoRecyclerView = findViewById<RecyclerView>(R.id.cofinhoRecyclerView)
        cofinhoRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val cofinhoItems = listOf(
            CofinhoItem(R.drawable.ic_porquinho, "Dia a Dia", "R$ 0,00", "Rendeu R$ 0,00"),
            CofinhoItem(R.drawable.ic_travel_bag, "Viagem", "R$ 0,00", "Rendeu R$ 0,00")
        )

        val adapter = CofinhoAdapter(cofinhoItems)
        cofinhoRecyclerView.adapter = adapter
    }

    private fun setupInvoiceModule() {
        val progressBar = findViewById<View>(R.id.home_progress_bar)
        val limiteUtilizado = findViewById<TextView>(R.id.home_limite_utilizado)
        val limiteDisponivel = findViewById<TextView>(R.id.home_limite_disponivel)
        val btnConferirFaturas = findViewById<TextView>(R.id.btn_conferir_faturas)

        // Calcular e exibir valores
        val disponivel = limiteTotal - limiteUsado

        limiteUtilizado.text = String.format("R$%.2f", limiteUsado).replace(".", ",")
        limiteDisponivel.text = String.format("R$%.2f", disponivel).replace(".", ",")

        // Calcular largura da barra de progresso
        val screenWidth = resources.displayMetrics.widthPixels - 64 // margins
        val progressWidth = ((limiteUsado / limiteTotal) * screenWidth).toInt()

        val params = progressBar.layoutParams
        params.width = progressWidth
        progressBar.layoutParams = params

        // Click para abrir tela de faturas
        btnConferirFaturas.setOnClickListener {
            val intent = Intent(this, InvoiceActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupActionButtons() {
        val btnPix = findViewById<MaterialCardView>(R.id.btn_pix)
        val btnCards = findViewById<MaterialCardView>(R.id.btn_cards)
        val investmentsCard = findViewById<MaterialCardView>(R.id.investmentsCard)

        btnPix.setOnClickListener {
            val intent = Intent(this, PixActivity::class.java)
            startActivity(intent)
        }

        btnCards.setOnClickListener {
            val intent = Intent(this, CardsActivity::class.java)
            startActivity(intent)
        }

        investmentsCard.setOnClickListener {
            val intent = Intent(this, PoupancaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupBottomNavigation() {
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navList = findViewById<ImageView>(R.id.nav_list)
        val navQr = findViewById<ImageView>(R.id.nav_qr)
        val navTransfer = findViewById<ImageView>(R.id.nav_transfer)
        val navSettings = findViewById<ImageView>(R.id.nav_settings)

        navHome.setOnClickListener {
            showToast("Início clicado")
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
            val intent = Intent(this, ConfiguracoesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}