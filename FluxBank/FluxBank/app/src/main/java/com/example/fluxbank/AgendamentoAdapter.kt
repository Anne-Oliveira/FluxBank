package com.example.fluxbank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class AgendamentoAdapter(
    private val agendamentos: MutableList<Agendamento>,
    private val onDelete: (Agendamento) -> Unit
) : RecyclerView.Adapter<AgendamentoAdapter.AgendamentoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendamentoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_agendamento, parent, false)
        return AgendamentoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgendamentoViewHolder, position: Int) {
        val agendamento = agendamentos[position]
        holder.bind(agendamento)
        holder.ivDelete.setOnClickListener {
            onDelete(agendamento)
        }
    }

    override fun getItemCount() = agendamentos.size

    class AgendamentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNome: TextView = itemView.findViewById(R.id.tvNomeDestinatario)
        private val tvData: TextView = itemView.findViewById(R.id.tvDataAgendamento)
        private val tvValor: TextView = itemView.findViewById(R.id.tvValorAgendamento)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDeleteAgendamento)

        fun bind(agendamento: Agendamento) {
            tvNome.text = agendamento.nomeDestinatario
            tvData.text = "Agendado para: ${agendamento.data}"
            tvValor.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(agendamento.valor)
        }
    }
}