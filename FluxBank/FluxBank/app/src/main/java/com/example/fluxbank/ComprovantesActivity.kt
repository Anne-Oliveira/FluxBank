package com.example.fluxbank

import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ComprovantesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprovantes)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewComprovantes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val items = listOf(
            ComprovanteItem.Data("Domingo, 30 de Novembro"),
            ComprovanteItem.Transacao("Pix", "Uber do Brasil Tecnologia Ltda", "-R$ 40,54"),
            ComprovanteItem.Transacao("Pix", "Uber do Brasil Tecnologia Ltda", "-R$ 40,54"),
            ComprovanteItem.Data("Sábado, 29 de Novembro"),
            ComprovanteItem.Transacao("Boleto", "Neoenergia Cosern", "-R$ 150,20"),
            ComprovanteItem.Transacao("Pix", "Restaurante do Zé", "-R$ 80,75")
        )

        recyclerView.adapter = ComprovantesAdapter(items)
    }
}