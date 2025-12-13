package com.example.fluxbank

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText

class CofinhoActivity : BaseActivity() {

    private var isSaldoVisible = false
    private val saldoOculto = "R$ ******"
    private val saldoVisivel = "R$ 150,00"

    private lateinit var cofinhoAdapter: CofinhoAdapter
    private val cofinhoItems = mutableListOf<CofinhoItem>()

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
        setContentView(R.layout.activity_cofinho)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val totalCofinhoValue = findViewById<TextView>(R.id.totalCofinhoValue)
        val visibilityIcon = findViewById<ImageView>(R.id.visibilityIcon)
        val btnCriarCofinho = findViewById<Button>(R.id.btnCriarCofinho)

        totalCofinhoValue.text = saldoOculto

        btnBack.setOnClickListener { finish() }

        visibilityIcon.setOnClickListener {
            isSaldoVisible = !isSaldoVisible
            if (isSaldoVisible) {
                totalCofinhoValue.text = saldoVisivel
                visibilityIcon.setImageResource(R.drawable.ic_visibility_off)
            } else {
                totalCofinhoValue.text = saldoOculto
                visibilityIcon.setImageResource(R.drawable.ic_visibility)
            }
        }

        setupCofinho()

        btnCriarCofinho.setOnClickListener {
            abrirBottomSheetCriarCofinho()
        }

        setupBottomNavigation()
    }

    private fun setupCofinho() {
        val recyclerView = findViewById<RecyclerView>(R.id.cofinhoRecyclerView)
        recyclerView.layoutManager =
            GridLayoutManager(this, 3)

        cofinhoItems.addAll(
            listOf(
                CofinhoItem(R.drawable.ic_porquinho, "Dia a Dia", "R$ 0,00", "Rendeu R$ 0,00"),
                CofinhoItem(R.drawable.ic_travel_bag, "Viagem", "R$ 0,00", "Rendeu R$ 0,00")
            )
        )

        cofinhoAdapter = CofinhoAdapter(cofinhoItems)
        recyclerView.adapter = cofinhoAdapter
    }

    private fun abrirBottomSheetCriarCofinho() {
        val bottomSheet = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_criar_cofinho, null)

        val etNomeCofinho = view.findViewById<TextInputEditText>(R.id.etNomeCofinho)
        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmarCriacao)

        btnConfirmar.setOnClickListener {
            val nome = etNomeCofinho.text.toString().trim()

            if (nome.isEmpty()) {
                showToast("Informe um nome para o cofrinho")
                return@setOnClickListener
            }

            val novoCofinho = CofinhoItem(
                R.drawable.ic_porquinho,
                nome,
                "R$ 0,00",
                "Rendeu R$ 0,00"
            )

            cofinhoAdapter.addCofinho(novoCofinho)
            bottomSheet.dismiss()
            showToast("Cofrinho criado com sucesso")
        }

        bottomSheet.setContentView(view)
        bottomSheet.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
