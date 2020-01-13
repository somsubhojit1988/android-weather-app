package com.example.weatherapplication.database

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {

    var data: T? = null
    val countDownLatch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(t: T?) {
            data = t
            countDownLatch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    try {
        afterObserve.invoke()

        if (!countDownLatch.await(time, timeUnit)) {
            throw IllegalArgumentException("LiveData value was never set")
        }
    } finally {
        this.removeObserver(observer)
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}