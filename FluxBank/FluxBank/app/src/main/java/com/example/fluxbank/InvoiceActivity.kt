package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InvoiceActivity : AppCompatActivity() {

    private lateinit var invoicesContainer: LinearLayout
    private lateinit var progressBar: View
    private lateinit var limiteUtilizado: TextView
    private lateinit var limiteDisponivel: TextView

    private val limiteTotal = 1400.0
    private val limiteUsado = 924.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)

        // Inicializar views
        initViews()

        // Configurar limite
        setupLimit()

        // Configurar faturas
        setupInvoices()

        // Configurar header
        setupHeader()

        // Configurar botões
        setupButtons()

        // Configurar bottom navigation
        setupBottomNavigation()
    }

    private fun initViews() {
        invoicesContainer = findViewById(R.id.invoices_container)
        progressBar = findViewById(R.id.progress_bar)
        limiteUtilizado = findViewById(R.id.limite_utilizado)
        limiteDisponivel = findViewById(R.id.limite_disponivel)
    }

    private fun setupLimit() {
        val disponivel = limiteTotal - limiteUsado

        limiteUtilizado.text = String.format("R$%.2f", limiteUsado).replace(".", ",")
        limiteDisponivel.text = String.format("R$%.2f", disponivel).replace(".", ",")

        // Calcular largura da barra de progresso
        val screenWidth = resources.displayMetrics.widthPixels - 32 // padding
        val progressWidth = ((limiteUsado / limiteTotal) * screenWidth).toInt()

        val params = progressBar.layoutParams
        params.width = progressWidth
        progressBar.layoutParams = params
    }

    private fun setupInvoices() {
        val invoices = listOf(
            Invoice(
                month = "Dezembro",
                value = "R$324,00",
                dueDate = "18/12",
                bestDay = "12/12",
                isOpen = true,
                canAnticipate = true
            ),
            Invoice(
                month = "Janeiro",
                value = "R$100,00",
                dueDate = "18/01",
                bestDay = null,
                isOpen = false,
                canAnticipate = false
            ),
            Invoice(
                month = "Fevereiro",
                value = "R$0,00",
                dueDate = "18/02",
                bestDay = null,
                isOpen = false,
                canAnticipate = false
            ),
            Invoice(
                month = "Março",
                value = "R$0,00",
                dueDate = "18/03",
                bestDay = null,
                isOpen = false,
                canAnticipate = false
            ),
            Invoice(
                month = "Abril",
                value = "R$0,00",
                dueDate = "18/04",
                bestDay = null,
                isOpen = false,
                canAnticipate = false
            )
        )

        // Adicionar cada fatura ao container
        invoices.forEach { invoice ->
            val invoiceView = createInvoiceView(invoice)
            invoicesContainer.addView(invoiceView)
        }
    }

    private fun createInvoiceView(invoice: Invoice): View {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.item_invoice, invoicesContainer, false)

        // Configurar views
        val monthText = view.findViewById<TextView>(R.id.invoice_month)
        val badgeText = view.findViewById<TextView>(R.id.invoice_badge)
        val valueText = view.findViewById<TextView>(R.id.invoice_value)
        val dueDateText = view.findViewById<TextView>(R.id.invoice_due_date)
        val bestDayContainer = view.findViewById<View>(R.id.best_day_container)
        val bestDayText = view.findViewById<TextView>(R.id.invoice_best_day)
        val btnAntecipar = view.findViewById<TextView>(R.id.btn_antecipar)

        // Preencher dados
        monthText.text = invoice.month
        valueText.text = invoice.value
        dueDateText.text = invoice.dueDate

        // Badge "Aberta"
        if (invoice.isOpen) {
            badgeText.visibility = View.VISIBLE
        } else {
            badgeText.visibility = View.GONE
        }

        // Melhor dia de compra
        if (invoice.bestDay != null) {
            bestDayContainer.visibility = View.VISIBLE
            bestDayText.text = invoice.bestDay
        } else {
            bestDayContainer.visibility = View.GONE
        }

        // Botão Antecipar
        if (invoice.canAnticipate) {
            btnAntecipar.visibility = View.VISIBLE
            btnAntecipar.setOnClickListener {
                showToast("Antecipar fatura de ${invoice.month}")
            }
        } else {
            btnAntecipar.visibility = View.GONE
        }

        // Click no card
        view.setOnClickListener {
            showToast("Fatura de ${invoice.month}: ${invoice.value}")
        }

        return view
    }

    private fun setupHeader() {
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnHelp = findViewById<ImageView>(R.id.btn_help)

        btnBack.setOnClickListener {
            finish()
        }

        btnHelp.setOnClickListener {
            showToast("Ajuda clicado")
        }
    }

    private fun setupButtons() {
        val btnMostrarDetalhes = findViewById<TextView>(R.id.btn_mostrar_detalhes)
        val btnMostrarTodas = findViewById<TextView>(R.id.btn_mostrar_todas)
        val btnMostrarCompras = findViewById<TextView>(R.id.btn_mostrar_compras)

        btnMostrarDetalhes.setOnClickListener {
            showToast("Mostrar detalhes do limite")
        }

        btnMostrarTodas.setOnClickListener {
            showToast("Mostrar todas as faturas")
        }

        btnMostrarCompras.setOnClickListener {
            showToast("Mostrar todas as compras")
        }
    }

    private fun setupBottomNavigation() {
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navList = findViewById<ImageView>(R.id.nav_list)
        val navQr = findViewById<ImageView>(R.id.nav_qr)
        val navTransfer = findViewById<ImageView>(R.id.nav_transfer)
        val navSettings = findViewById<ImageView>(R.id.nav_settings)

        navHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        navList.setOnClickListener {
            showToast("Lista clicado")
        }

        navQr.setOnClickListener {
            showToast("QR Code clicado")
        }

        navTransfer.setOnClickListener {
            val intent = Intent(this, PixActivity::class.java)
            startActivity(intent)
        }

        navSettings.setOnClickListener {
            showToast("Configurações clicado")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}