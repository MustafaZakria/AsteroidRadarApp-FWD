package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.repo.AsteroidRepository
import com.udacity.asteroidradar.utils.Constants.WORK_NAME
import com.udacity.asteroidradar.work.DailyWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication : Application() {

    //private val apiService by lazy { AsteroidApiService.retrofitService }

    private val database by lazy { AsteroidDatabase.getInstance(this) }

    val repository by lazy { AsteroidRepository(database.dao) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.Default).launch {
            setupDailyWork()
        }
    }

    private fun setupDailyWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .setRequiresStorageNotLow(true)
            .build()

        val refreshRequest = PeriodicWorkRequestBuilder<DailyWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, // Will Neglect the newly created request
            refreshRequest
        )
    }
}