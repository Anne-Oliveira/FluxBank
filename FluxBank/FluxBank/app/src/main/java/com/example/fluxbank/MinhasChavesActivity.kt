package com.example.fluxbank

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fluxbank.utils.TokenManager
import kotlinx.coroutines.launch

class MinhasChavesActivity : BaseActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minhas_chaves)

        tokenManager = TokenManager(this)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val txtChavePix = findViewById<TextView>(R.id.txtChavePix)

        btnBack.setOnClickListener {
            finish()
        }

        carregarMinhaChave()
    }

    private fun carregarMinhaChave() {
        lifecycleScope.launch {
            try {
                val userCpf = tokenManager.getUserCpf()
                val userCnpj = tokenManager.getUserCnpj()

                val chavePix = userCpf ?: userCnpj ?: "Não disponível"

                findViewById<TextView>(R.id.txtChavePix).text = chavePix
                findViewById<TextView>(R.id.txtTipoChave).text =
                    if (userCpf != null) "CPF" else if (userCnpj != null) "CNPJ" else "N/A"

                Log.d("MinhasChaves", "Chave Pix: $chavePix")

            } catch (e: Exception) {
                Log.e("MinhasChaves", "Erro ao carregar chave", e)
                Toast.makeText(this@MinhasChavesActivity, "Erro ao carregar chave", Toast.LENGTH_SHORT).show()
            }
        }
    }
}