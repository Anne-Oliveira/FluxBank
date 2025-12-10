package com.example.fluxbank

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

data class PoupancaItem(
    val title: String,
    val account: String,
    var totalValue: Double,
    var rescueValue: Double
)

class PoupancaAdapter(private val items: MutableList<PoupancaItem>) : RecyclerView.Adapter<PoupancaAdapter.PoupancaViewHolder>() {

    private var isSaldoVisible = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoupancaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poupanca, parent, false)
        return PoupancaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PoupancaViewHolder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context

        holder.poupancaTitle.text = item.title
        holder.poupancaAccount.text = item.account

        updateSaldoView(holder, item)

        holder.btnAplicar.setOnClickListener {
            showValorDialog(context, "Aplicar na Poupança", "Quanto você deseja aplicar?", true, item, position)
        }

        holder.btnResgatar.setOnClickListener {
            showValorDialog(context, "Resgatar da Poupança", "Quanto você deseja resgatar?", false, item, position)
        }
    }

    private fun updateSaldoView(holder: PoupancaViewHolder, item: PoupancaItem) {
        val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        if (isSaldoVisible) {
            holder.valorTotalValue.text = format.format(item.totalValue)
            holder.valorResgateValue.text = format.format(item.rescueValue)
        } else {
            holder.valorTotalValue.text = "R$ ******"
            holder.valorResgateValue.text = "R$ ******"
        }
    }

    private fun showValorDialog(context: Context, title: String, message: String, isAplicacao: Boolean, item: PoupancaItem, position: Int) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_valor, null)
        val editText = dialogView.findViewById<EditText>(R.id.edtValor)

        builder.setView(dialogView)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(if (isAplicacao) "Aplicar" else "Resgatar") { _, _ ->
                val valorStr = editText.text.toString()
                if (valorStr.isNotEmpty()) {
                    val valor = valorStr.toDouble()
                    if (isAplicacao) {
                        aplicarValor(context, item, valor, position)
                    } else {
                        resgatarValor(context, item, valor, position)
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun aplicarValor(context: Context, item: PoupancaItem, valor: Double, position: Int) {
        item.totalValue += valor
        item.rescueValue += valor
        notifyItemChanged(position)
        Toast.makeText(context, "Aplicação realizada com sucesso!", Toast.LENGTH_SHORT).show()
    }

    private fun resgatarValor(context: Context, item: PoupancaItem, valor: Double, position: Int) {
        if (valor > item.rescueValue) {
            Toast.makeText(context, "Saldo insuficiente para resgate.", Toast.LENGTH_SHORT).show()
            return
        }
        item.totalValue -= valor
        item.rescueValue -= valor
        notifyItemChanged(position)
        Toast.makeText(context, "Resgate realizado com sucesso!", Toast.LENGTH_SHORT).show()
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