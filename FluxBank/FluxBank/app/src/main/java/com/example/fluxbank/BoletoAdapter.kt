package com.example.fluxbank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BoletoAdapter(
    private val boletos: List<BoletoItem>
) : RecyclerView.Adapter<BoletoAdapter.BoletoViewHolder>() {

    inner class BoletoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val subtitle: TextView = itemView.findViewById(R.id.subtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoletoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_boleto, parent, false)
        return BoletoViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoletoViewHolder, position: Int) {
        val boleto = boletos[position]
        holder.title.text = boleto.title
        holder.subtitle.text = boleto.subtitle
    }

    override fun getItemCount(): Int = boletos.size
}
