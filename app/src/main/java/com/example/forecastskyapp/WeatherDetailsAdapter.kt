package com.example.forecastskyapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forecastskyapp.R
import com.example.weatherapp.model.WeatherDetail

class WeatherDetailsAdapter(
    private val details: List<WeatherDetail>
) : RecyclerView.Adapter<WeatherDetailsAdapter.DetailViewHolder>() {

    class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.iv_detail_icon)
        val label: TextView = view.findViewById(R.id.tv_detail_label)
        val value: TextView = view.findViewById(R.id.tv_detail_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather_details, parent, false)
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val item = details[position]
        holder.label.text = item.label
        holder.value.text = item.value
        holder.icon.setImageResource(item.iconResId)
    }

    override fun getItemCount(): Int = details.size
}
