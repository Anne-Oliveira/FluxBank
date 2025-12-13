package com.example.fluxbank

import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class BoletoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boletos)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerBoletos)

        btnBack.setOnClickListener { finish() }

        recyclerView.layoutManager = LinearLayoutManager(this)

        val boletos = listOf(
            BoletoItem("EDUCARE TECNOLOGIA", "Vence em 12/01/26"),
            BoletoItem("EDUCARE TECNOLOGIA", "Vence em 12/02/26"),
            BoletoItem("EDUCARE TECNOLOGIA", "Vence em 12/02/26")
        )

        recyclerView.adapter = BoletoAdapter(boletos)

        tabLayout.addTab(tabLayout.newTab().setText("A pagar"))
        tabLayout.addTab(tabLayout.newTab().setText("Autorizados"))
        tabLayout.addTab(tabLayout.newTab().setText("Ocultos"))
    }
}
