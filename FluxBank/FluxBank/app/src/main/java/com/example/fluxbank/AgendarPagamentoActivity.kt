package com.example.fluxbank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AgendarPagamentoActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var adapter: AgendamentoAdapter
    private val agendamentos = mutableListOf<Agendamento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendar_pagamento)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        recyclerView = findViewById(R.id.recyclerViewAgendamentos)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        val fab = findViewById<FloatingActionButton>(R.id.fabAdicionarAgendamento)

        btnBack.setOnClickListener {
            finish()
        }

        fab.setOnClickListener {
            showAdicionarAgendamentoDialog()
        }

        setupRecyclerView()
        updateEmptyState()
    }

    private fun setupRecyclerView() {
        adapter = AgendamentoAdapter(agendamentos) { agendamento ->
            // Lógica de exclusão
            val position = agendamentos.indexOf(agendamento)
            agendamentos.removeAt(position)
            adapter.notifyItemRemoved(position)
            updateEmptyState()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun showAdicionarAgendamentoDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_agendamento, null)

        val edtNome = dialogView.findViewById<EditText>(R.id.edtNomeDestinatario)
        val edtValor = dialogView.findViewById<EditText>(R.id.edtValorAgendamento)
        val edtData = dialogView.findViewById<EditText>(R.id.edtDataAgendamento)

        builder.setView(dialogView)
            .setTitle("Agendar Pagamento")
            .setPositiveButton("Agendar") { _, _ ->
                val nome = edtNome.text.toString()
                val valor = edtValor.text.toString().toDoubleOrNull() ?: 0.0
                val data = edtData.text.toString()

                if (nome.isNotEmpty() && valor > 0 && data.isNotEmpty()) {
                    val novoAgendamento = Agendamento(nome, valor, data)
                    agendamentos.add(novoAgendamento)
                    adapter.notifyItemInserted(agendamentos.size - 1)
                    updateEmptyState()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun updateEmptyState() {
        if (agendamentos.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyStateLayout.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyStateLayout.visibility = View.GONE
        }
    }
}