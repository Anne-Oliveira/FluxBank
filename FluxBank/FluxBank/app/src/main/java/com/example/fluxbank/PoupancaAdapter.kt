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
    val totalValueVisible: String,
    val totalValueHidden: String,
    val rescueValueVisible: String,
    val rescueValueHidden: String
)

class PoupancaAdapter(private val items: List<PoupancaItem>) : RecyclerView.Adapter<PoupancaAdapter.PoupancaViewHolder>() {

    private var isSaldoVisible = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoupancaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poupanca, parent, false)
        return PoupancaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PoupancaViewHolder, position: Int) {
        val item = items[position]
        holder.poupancaTitle.text = item.title
        holder.poupancaAccount.text = item.account

        if (isSaldoVisible) {
            holder.valorTotalValue.text = item.totalValueVisible
            holder.valorResgateValue.text = item.rescueValueVisible
        } else {
            holder.valorTotalValue.text = item.totalValueHidden
            holder.valorResgateValue.text = item.rescueValueHidden
        }
    }

    override fun getItemCount() = items.size

    fun toggleSaldoVisibility() {
        isSaldoVisible = !isSaldoVisible
        notifyDataSetChanged()
    }

    class PoupancaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poupancaTitle: TextView = itemView.findViewById(R.id.poupancaTitle)
        val poupancaAccount: TextView = itemView.findViewById(R.id.poupancaAccount)
        val valorTotalValue: TextView = itemView.findViewById(R.id.valorTotalValue)
        val valorResgateValue: TextView = itemView.findViewById(R.id.valorResgateValue)
        val btnResgatar: Button = itemView.findViewById(R.id.btnResgatar)
        val btnAplicar: Button = itemView.findViewById(R.id.btnAplicar)
    }
}
