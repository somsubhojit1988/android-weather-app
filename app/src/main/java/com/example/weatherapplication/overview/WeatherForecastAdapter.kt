package com.example.weatherapplication.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.databinding.ForecastListItemBinding
import com.example.weatherapplication.model.Forecast

class ForecastViewHolder private constructor(private val binding: ForecastListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(item: Forecast, clickListener: ForecastListener) {
        binding.forecast = item
        binding.forecastListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ForecastViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ForecastListItemBinding.inflate(layoutInflater, parent, false)
            return ForecastViewHolder(binding)
        }
    }
}

class WeatherForecastAdapter(val clickListener: ForecastListener) :
    ListAdapter<Forecast, ForecastViewHolder>(ForecastDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ForecastViewHolder.from(parent)


    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) =
        holder.bind(getItem(position), clickListener)
}

class ForecastListener(val clickListener: (Long) -> Unit) {
    fun onClick(forecast: Forecast) {
        clickListener(forecast.dt)
    }
}


class ForecastDiffCallback : DiffUtil.ItemCallback<Forecast>() {

    override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast) =
        oldItem.dt == newItem.dt

    override fun areContentsTheSame(oldItem: Forecast, newItem: Forecast): Boolean =
        oldItem == newItem

}