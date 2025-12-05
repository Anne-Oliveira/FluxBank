package com.example.fluxbank

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

data class CardAction(val iconRes: Int, val title: String)

class CardActionsAdapter(
    private val context: Context,
    private val actions: List<CardAction>
) : BaseAdapter() {

    override fun getCount(): Int = actions.size

    override fun getItem(position: Int): Any = actions[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_card_action, parent, false)

        val action = actions[position]
        val iconImage = view.findViewById<ImageView>(R.id.action_icon)
        val titleText = view.findViewById<TextView>(R.id.action_title)

        iconImage.setImageResource(action.iconRes)
        titleText.text = action.title

        return view
    }
}