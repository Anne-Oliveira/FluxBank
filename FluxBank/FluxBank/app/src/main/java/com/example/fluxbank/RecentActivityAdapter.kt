package com.example.fluxbank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class RecentActivity(val description: String, val type: String, val value: String)

class RecentActivityAdapter(private val activities: List<RecentActivity>) : RecyclerView.Adapter<RecentActivityAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description: TextView = view.findViewById(R.id.activityDescription)
        val type: TextView = view.findViewById(R.id.activityType)
        val value: TextView = view.findViewById(R.id.activityValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_activity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activities[position]
        holder.description.text = activity.description
        holder.type.text = activity.type
        holder.value.text = activity.value
    }

    override fun getItemCount() = activities.size
}
