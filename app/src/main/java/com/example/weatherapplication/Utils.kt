package com.example.weatherapplication

import com.example.weatherapplication.network.WeatherResponse
import com.example.weatherapplication.overview.Forecast
import java.text.SimpleDateFormat
import java.util.*

fun weatherResponseToForecastList(res: WeatherResponse): List<Forecast> {
    val list = mutableListOf<Forecast>()
    for (data in res.daily.data.subList(1, res.daily.data.size)) {
        list.add(
            Forecast(
                data.time,
                data.summary,
                data.temperatureMax,
                data.temperatureMin,
                data.icon
            )
        )
    }
    return list
}

// lesson: epoch time has to be in milli-seconds for this to work
const val EPOCH_FMT_STR = "MM/dd/yyyy, EEEE"

fun Long.formatDate(): String {
   return SimpleDateFormat(EPOCH_FMT_STR, Locale.getDefault()).format(this * 1000)
}
