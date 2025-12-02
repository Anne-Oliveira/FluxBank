package com.example.fluxbank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class PoupancaItem(
    val title: String,
    val account: String,
    val totalValue: String,
    val rescueValue: String
)

class PoupancaAdapter(private val items: List<PoupancaItem>) : RecyclerView.Adapter<PoupancaAdapter.PoupancaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoupancaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poupanca, parent, false)
        return PoupancaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PoupancaViewHolder, position: Int) {
        val item = items[position]
        holder.poupancaTitle.text = item.title
        holder.poupancaAccount.text = item.account
        holder.valorTotalValue.text = item.totalValue
        holder.valorResgateValue.text = item.rescueValue
    }

    override fun getItemCount() = items.size

    class PoupancaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poupancaTitle: TextView = itemView.findViewById(R.id.poupancaTitle)
        val poupancaAccount: TextView = itemView.findViewById(R.id.poupancaAccount)
        val valorTotalValue: TextView = itemView.findViewById(R.id.valorTotalValue)
        val valorResgateValue: TextView = itemView.findViewById(R.id.valorResgateValue)
        val btnResgatar: Button = itemView.findViewById(R.id.btnResgatar)
        val btnAplicar: Button = itemView.findViewById(R.id.btnAplicar)
    }
}
