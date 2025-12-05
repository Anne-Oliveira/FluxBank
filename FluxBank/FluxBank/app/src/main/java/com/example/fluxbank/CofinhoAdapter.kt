package com.example.fluxbank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class CofinhoItem(
    val iconResId: Int,
    val title: String,
    val value: String,
    val rendeu: String
)

class CofinhoAdapter(private val items: List<CofinhoItem>) : RecyclerView.Adapter<CofinhoAdapter.CofinhoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CofinhoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cofinho, parent, false)
        return CofinhoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CofinhoViewHolder, position: Int) {
        val item = items[position]
        holder.cofinhoIcon.setImageResource(item.iconResId)
        holder.cofinhoTitle.text = item.title
        holder.cofinhoValue.text = item.value
        holder.cofinhoRendeu.text = item.rendeu
    }

    override fun getItemCount() = items.size

    class CofinhoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cofinhoIcon: ImageView = itemView.findViewById(R.id.cofinhoIcon)
        val cofinhoTitle: TextView = itemView.findViewById(R.id.cofinhoTitle)
        val cofinhoValue: TextView = itemView.findViewById(R.id.cofinhoValue)
        val cofinhoRendeu: TextView = itemView.findViewById(R.id.cofinhoRendeu)
    }
}
