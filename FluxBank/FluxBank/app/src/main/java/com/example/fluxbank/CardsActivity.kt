package com.example.fluxbank

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlin.math.abs

class CardsActivity : BaseActivity() {

    private lateinit var cardImage: ImageView
    private lateinit var cardType: TextView
    private lateinit var cardNumber: TextView
    private lateinit var btnPrevious: ImageView
    private lateinit var btnNext: ImageView

    private lateinit var dot0: View
    private lateinit var dot1: View
    private lateinit var dot2: View

    private var currentCardIndex = 0
    private lateinit var cards: MutableList<CardInfo>

    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)

        initViews()

        setupCards()

        displayCard(currentCardIndex)

        setupSwipeGesture()

        setupHeader()

        setupCardActions()

        setupCardButtons()

        setupCardNavigation()

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
        cards = mutableListOf(
            CardInfo(
                type = "Débito virtual",
                number = "•••• 8922",
                imageRes = R.drawable.card_debit,
                gradientStart = "#1a0033",
                gradientCenter = "#4d0066",
                gradientEnd = "#800080",
                isBlocked = false
            ),
            CardInfo(
                type = "Crédito",
                number = "•••• 5431",
                imageRes = R.drawable.card_credit,
                gradientStart = "#1a0033",
                gradientCenter = "#4d0066",
                gradientEnd = "#800080",
                isBlocked = false
            ),
            CardInfo(
                type = "Virtual",
                number = "•••• 7689",
                imageRes = R.drawable.card_virtual,
                gradientStart = "#1a0033",
                gradientCenter = "#4d0066",
                gradientEnd = "#800080",
                isBlocked = false
            )
        )
    }

    private fun setupSwipeGesture() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {

            private val SWIPE_THRESHOLD = 100
            private val SWIPE_VELOCITY_THRESHOLD = 100

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {

                if (e1 == null) return false

                val diffX = e2.x - e1.x
                val diffY = e2.y - e1.y

                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        return true
                    }
                }
                return false
            }
        })

        cardImage.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun onSwipeLeft() {
        if (currentCardIndex < cards.size - 1) {
            Log.d("CardsActivity", "Swipe LEFT - Próximo cartão")
            animateCardTransition(fromRight = false)
            currentCardIndex++
            displayCard(currentCardIndex)
        }
    }

    private fun onSwipeRight() {
        if (currentCardIndex > 0) {
            Log.d("CardsActivity", "Swipe RIGHT - Cartão anterior")
            animateCardTransition(fromRight = true)
            currentCardIndex--
            displayCard(currentCardIndex)
        }
    }

    private fun animateCardTransition(fromRight: Boolean) {
        val startX = if (fromRight) -cardImage.width.toFloat() else cardImage.width.toFloat()

        ObjectAnimator.ofFloat(cardImage, "alpha", 1f, 0f).apply {
            duration = 150
            start()
        }

        cardImage.translationX = startX
        ObjectAnimator.ofFloat(cardImage, "translationX", startX, 0f).apply {
            duration = 300
            start()
        }

        ObjectAnimator.ofFloat(cardImage, "alpha", 0f, 1f).apply {
            duration = 150
            startDelay = 150
            start()
        }
    }

    private fun displayCard(index: Int) {
        val card = cards[index]

        Log.d("CardsActivity", "=== EXIBINDO CARTÃO ===")
        Log.d("CardsActivity", "Tipo: ${card.type}")
        Log.d("CardsActivity", "Número: ${card.number}")
        Log.d("CardsActivity", "Bloqueado: ${card.isBlocked}")

        cardType.text = if (card.isBlocked) "${card.type} (Bloqueado)" else card.type
        cardNumber.text = card.number
        cardImage.setImageResource(card.imageRes)

        if (card.isBlocked) {
            cardImage.alpha = 0.5f
            cardType.setTextColor(getColor(android.R.color.holo_red_dark))
        } else {
            cardImage.alpha = 1.0f
            cardType.setTextColor(getColor(android.R.color.white))
        }

        updateDots(index)

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
                animateCardTransition(fromRight = true)
                currentCardIndex--
                displayCard(currentCardIndex)
            }
        }

        btnNext.setOnClickListener {
            if (currentCardIndex < cards.size - 1) {
                animateCardTransition(fromRight = false)
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

            when (position) {
                0 -> bloquearDesbloquearCartao()
                1 -> cancelarCriarNovo()
            }
        }
    }

    private fun bloquearDesbloquearCartao() {
        val card = cards[currentCardIndex]

        if (card.isBlocked) {
            AlertDialog.Builder(this)
                .setTitle("Desbloquear cartão")
                .setMessage("Deseja desbloquear o cartão ${card.type} ${card.number}?")
                .setPositiveButton("Desbloquear") { _, _ ->
                    card.isBlocked = false
                    displayCard(currentCardIndex)

                    Toast.makeText(
                        this,
                        "Cartão desbloqueado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.d("CardsActivity", "Cartão desbloqueado: ${card.type}")
                }
                .setNegativeButton("Cancelar", null)
                .show()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Bloquear cartão")
                .setMessage("Tem certeza que deseja bloquear o cartão ${card.type} ${card.number}?\n\nVocê não poderá usá-lo até desbloquear.")
                .setPositiveButton("Bloquear") { _, _ ->
                    card.isBlocked = true
                    displayCard(currentCardIndex)

                    Toast.makeText(
                        this,
                        "Cartão bloqueado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.d("CardsActivity", "Cartão bloqueado: ${card.type}")
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun cancelarCriarNovo() {
        val card = cards[currentCardIndex]

        AlertDialog.Builder(this)
            .setTitle("Cancelar cartão")
            .setMessage("Deseja cancelar o cartão ${card.type} ${card.number} e criar um novo?\n\nEsta ação é irreversível.")
            .setPositiveButton("Confirmar") { _, _ ->
                Toast.makeText(
                    this,
                    "Cancelar e criar novo (em desenvolvimento)",
                    Toast.LENGTH_LONG
                ).show()

                Log.d("CardsActivity", "Solicitar cancelamento: ${card.type}")
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun setupCardButtons() {
        val btnCopy = findViewById<ImageView>(R.id.btn_copy)
        val btnViewDetails = findViewById<TextView>(R.id.btn_view_details)

        btnCopy.setOnClickListener {
            val card = cards[currentCardIndex]

            if (card.isBlocked) {
                showToast("Cartão bloqueado! Não é possível copiar.")
            } else {
                showToast("Número ${card.number} copiado!")
            }
        }

        btnViewDetails.setOnClickListener {
            val card = cards[currentCardIndex]

            if (card.isBlocked) {
                showToast("Cartão bloqueado! Desbloqueie para ver detalhes.")
            } else {
                showToast("Ver dados do cartão ${card.type}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}