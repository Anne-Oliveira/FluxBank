package com.example.fluxbank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FaqAdapter(private val listaFaq: List<FaqItem>) : RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    inner class FaqViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textoPergunta: TextView = itemView.findViewById(R.id.textoPergunta)
        val textoResposta: TextView = itemView.findViewById(R.id.textoResposta)
        val iconExpandir: ImageView = itemView.findViewById(R.id.iconExpandir)
        val perguntaContainer: View = itemView.findViewById(R.id.perguntaContainer)
        val respostaContainer: LinearLayout = itemView.findViewById(R.id.respostaContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq, parent, false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val faqItem = listaFaq[position]

        holder.textoPergunta.text = faqItem.pergunta
        holder.textoResposta.text = faqItem.resposta

        // Define visibilidade inicial e ícone
        if (faqItem.expandido) {
            holder.respostaContainer.visibility = View.VISIBLE
            holder.iconExpandir.setImageResource(R.drawable.ic_remove) // Ícone de menos
        } else {
            holder.respostaContainer.visibility = View.GONE
            holder.iconExpandir.setImageResource(R.drawable.ic_add) // Ícone de mais
        }

        // Click para expandir/recolher
        holder.perguntaContainer.setOnClickListener {
            val expandir = !faqItem.expandido
            faqItem.expandido = expandir

            if (expandir) {
                // Expandir
                holder.respostaContainer.visibility = View.VISIBLE
                holder.iconExpandir.setImageResource(R.drawable.ic_remove)

                // Animação de expansão suave
                holder.respostaContainer.alpha = 0f
                holder.respostaContainer.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .start()

            } else {
                // Recolher
                holder.iconExpandir.setImageResource(R.drawable.ic_add)

                // Animação de recolhimento suave
                holder.respostaContainer.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction {
                        holder.respostaContainer.visibility = View.GONE
                    }
                    .start()
            }
        }
    }

    override fun getItemCount(): Int = listaFaq.size
}