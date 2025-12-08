package com.example.fluxbank

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CardsActivity : AppCompatActivity() {

    private lateinit var cardImage: ImageView
    private lateinit var cardType: TextView
    private lateinit var cardNumber: TextView
    private lateinit var btnPrevious: ImageView
    private lateinit var btnNext: ImageView

    private lateinit var dot0: View
    private lateinit var dot1: View
    private lateinit var dot2: View

    private var currentCardIndex = 0
    private lateinit var cards: List<CardInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)

        // Inicializar views
        initViews()

        // Configurar lista de cartões
        setupCards()

        // Mostrar primeiro cartão
        displayCard(currentCardIndex)

        // Configuração do header
        setupHeader()

        // Configurar grid de ações
        setupCardActions()

        // Configurar botões da tela
        setupCardButtons()

        // Configurar navegação de cartões
        setupCardNavigation()

        // Configuração da navegação inferior
        setupBottomNavigation()
    }

    private fun initViews() {
        cardImage = findViewById(R.id.card_image)
        cardType = findViewById(R.id.card_type)
        cardNumber = findViewById(R.id.card_number)
        btnPrevious = findViewById(R.id.btn_previous_card)
        btnNext = findViewById(R.id.btn_next_card)

        dot0 = findViewById(R.id.dot_0)
        dot1 = findViewById(R.id.dot_1)
        dot2 = findViewById(R.id.dot_2)
    }

    private fun setupCards() {
        cards = listOf(
            CardInfo(
                type = "Débito virtual",
                number = "•••• 8922",
                imageRes = R.drawable.card_debit,
                gradientStart = "#1a0033",
                gradientCenter = "#4d0066",
                gradientEnd = "#800080"
            ),
            CardInfo(
                type = "Crédito",
                number = "•••• 5431",
                imageRes = R.drawable.card_credit,
                gradientStart = "#1a0033",
                gradientCenter = "#4d0066",
                gradientEnd = "#800080"
            ),
            CardInfo(
                type = "Virtual",
                number = "•••• 7689",
                imageRes = R.drawable.card_virtual,
                gradientStart = "#1a0033",
                gradientCenter = "#4d0066",
                gradientEnd = "#800080"
            )
        )
    }

    private fun displayCard(index: Int) {
        val card = cards[index]

        // Atualizar informações do cartão
        cardType.text = card.type
        cardNumber.text = card.number
        cardImage.setImageResource(card.imageRes)

        // Atualizar indicadores (dots)
        updateDots(index)

        // Atualizar visibilidade dos botões de navegação
        btnPrevious.visibility = if (index > 0) View.VISIBLE else View.INVISIBLE
        btnNext.visibility = if (index < cards.size - 1) View.VISIBLE else View.INVISIBLE
    }

    private fun updateDots(activeIndex: Int) {
        val dots = listOf(dot0, dot1, dot2)

        dots.forEachIndexed { index, dot ->
            if (index == activeIndex) {
                dot.setBackgroundResource(R.drawable.dot_indicator_selected)
            } else {
                dot.setBackgroundResource(R.drawable.dot_indicator)
            }
        }
    }

    private fun setupCardNavigation() {
        btnPrevious.setOnClickListener {
            if (currentCardIndex > 0) {
                currentCardIndex--
                displayCard(currentCardIndex)
            }
        }

        btnNext.setOnClickListener {
            if (currentCardIndex < cards.size - 1) {
                currentCardIndex++
                displayCard(currentCardIndex)
            }
        }
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

    private fun setupCardActions() {
        val actionsGrid = findViewById<GridView>(R.id.card_actions_grid)

        val actions = listOf(
            CardAction(R.drawable.ic_lock, "Bloquear cartão"),
            CardAction(R.drawable.ic_cancel_circle, "Cancelar e criar novo")
        )

        val adapter = CardActionsAdapter(this, actions)
        actionsGrid.adapter = adapter

        actionsGrid.setOnItemClickListener { _, _, position, _ ->
            val action = actions[position]
            showToast("${action.title} clicado")
        }
    }

    private fun setupCardButtons() {
        val btnCopy = findViewById<ImageView>(R.id.btn_copy)
        val btnViewDetails = findViewById<TextView>(R.id.btn_view_details)

        btnCopy.setOnClickListener {
            val card = cards[currentCardIndex]
            showToast("Número ${card.number} copiado!")
        }

        btnViewDetails.setOnClickListener {
            showToast("Ver dados do cartão ${cards[currentCardIndex].type}")
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
            val intent = Intent(this, LeitorQrActivity::class.java)
            startActivity(intent)
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