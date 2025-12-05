package com.example.fluxbank

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

data class PixOption(val iconRes: Int, val title: String)

class OptionsGridAdapter(
    private val context: Context,
    private val options: List<PixOption>
) : BaseAdapter() {

    override fun getCount(): Int = options.size

    override fun getItem(position: Int): Any = options[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_option_grid, parent, false)

        val option = options[position]
        val iconImage = view.findViewById<ImageView>(R.id.option_icon)
        val titleText = view.findViewById<TextView>(R.id.option_title)

        iconImage.setImageResource(option.iconRes)
        titleText.text = option.title

        return view
    }
}