package com.example.fluxbank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fluxbank.network.models.ContatoRecente

class RecentContactsAdapter(
    private val contatos: List<ContatoRecente>,
    private val onContatoClick: (ContatoRecente) -> Unit
) : RecyclerView.Adapter<RecentContactsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgAvatar: ImageView = view.findViewById(R.id.avatar_image)
        val txtNome: TextView = view.findViewById(R.id.name_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contato = contatos[position]

        val nomeCompleto = contato.nome?.takeIf { it.isNotBlank() } ?: "Contato"
        val primeiroNome = nomeCompleto.split(" ").firstOrNull() ?: "Contato"

        holder.txtNome.text = primeiroNome

        holder.itemView.setOnClickListener {
            onContatoClick(contato)
        }
    }

    override fun getItemCount() = contatos.size
}