package com.example.fluxbank

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ExtratoAdapter(private val context: Context, private val itens: List<ExtratoItem>)
    : ArrayAdapter<ExtratoItem>(context, 0, itens) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_extrato, parent, false)

        val item = itens[position]

        view.findViewById<TextView>(R.id.txtData).text = item.data
        view.findViewById<TextView>(R.id.txtTitulo).text = item.titulo
        view.findViewById<TextView>(R.id.txtSub).text = item.descricao
        view.findViewById<TextView>(R.id.txtValor).text = item.valor

        view.findViewById<ImageView>(R.id.iconCircle).setImageResource(item.icone)

        return view
    }
}



