package com.example.weatherapplication

import java.text.SimpleDateFormat
import java.util.*

// lesson: epoch time has to be in milli-seconds for this to work
private const val EPOCH_FMT_STR = "MM/dd/yyyy, EEEE"

fun Long.formatDate(): String {
    return SimpleDateFormat(EPOCH_FMT_STR, Locale.getDefault()).format(this * 1000)
}
