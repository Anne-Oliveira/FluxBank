package com.example.fluxbank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CofinhoAdapter(
    private val items: MutableList<CofinhoItem>
) : RecyclerView.Adapter<CofinhoAdapter.ViewHolder>() {

    fun addCofinho(item: CofinhoItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun setItems(newItems: List<CofinhoItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cofinho, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.cofinhoIcon.setImageResource(item.icon)
        holder.cofinhoTitle.text = item.nome
        holder.cofinhoValue.text = item.valor
        holder.cofinhoRendeu.text = item.rendeu
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cofinhoIcon: ImageView = itemView.findViewById(R.id.cofinhoIcon)
        val cofinhoTitle: TextView = itemView.findViewById(R.id.cofinhoTitle)
        val cofinhoValue: TextView = itemView.findViewById(R.id.cofinhoValue)
        val cofinhoRendeu: TextView = itemView.findViewById(R.id.cofinhoRendeu)
    }
}
