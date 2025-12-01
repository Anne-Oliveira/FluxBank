package com.example.fluxbank

import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView

object ListViewHelper {
    /**
     * Ajusta a altura do ListView para mostrar todos os itens
     * Útil quando o ListView está dentro de outro ScrollView ou layout scroll
     */
    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter: ListAdapter = listView.adapter ?: return

        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.UNSPECIFIED)

        for (i in 0 until listAdapter.count) {
            val listItem: View = listAdapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }

        val params: ViewGroup.LayoutParams = listView.layoutParams
        params.height = totalHeight + (listView.dividerHeight * (listAdapter.count - 1))
        listView.layoutParams = params
        listView.requestLayout()
    }
}