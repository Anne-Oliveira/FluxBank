package com.example.fluxbank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ComprovantesAdapter(private val items: List<ComprovanteItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TIPO_DATA = 0
        private const val TIPO_TRANSACAO = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ComprovanteItem.Data -> TIPO_DATA
            is ComprovanteItem.Transacao -> TIPO_TRANSACAO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TIPO_DATA) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comprovante_data, parent, false)
            DataViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comprovante_transacao, parent, false)
            TransacaoViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ComprovanteItem.Data -> (holder as DataViewHolder).bind(item)
            is ComprovanteItem.Transacao -> (holder as TransacaoViewHolder).bind(item)
        }
    }

    override fun getItemCount() = items.size

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvData: TextView = itemView.findViewById(R.id.tvData)
        fun bind(item: ComprovanteItem.Data) {
            tvData.text = item.data
        }
    }

    class TransacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTipo: TextView = itemView.findViewById(R.id.tvTipoTransacao)
        private val tvNome: TextView = itemView.findViewById(R.id.tvNomeEmpresa)
        private val tvValor: TextView = itemView.findViewById(R.id.tvValor)

        fun bind(item: ComprovanteItem.Transacao) {
            tvTipo.text = item.tipo
            tvNome.text = item.nome
            tvValor.text = item.valor
        }
    }
}