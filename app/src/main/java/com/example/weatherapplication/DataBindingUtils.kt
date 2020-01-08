package com.example.weatherapplication

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("bindDate")
fun bindDate(textView: TextView, dt: Long) {
    textView.text = dt.formatDate()
}

@BindingAdapter("weatherImage")
fun ImageView.bindWeatherImage(type: String?) {
    type?.let {
        setImageResource(
            when (type) {
                "clear-day" -> R.drawable.ic_clear_day
                "clear-night" -> R.drawable.ic_clear_night
                "rain" -> R.drawable.ic_rain
                "snow" -> R.drawable.ic_snow
                "sleet" -> R.drawable.ic_new_sleet
                "wind" -> R.drawable.ic_wind
                "fog" -> R.drawable.ic_fog
                "cloudy" -> R.drawable.ic_cloudy
                "partly-cloudy-day" -> R.drawable.ic_partly_cloudy_day
                "partly-cloudy-night" -> R.drawable.ic_partly_cloudy_night
                else -> R.drawable.ic_weather_general
            }
        )
    }
}