package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.repo.AsteroidRepository
import retrofit2.HttpException

class DailyWorker(
    appContext: Context,
    params: WorkerParameters,
    private val repository: AsteroidRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = try {
        repository.refreshAsteroids()
        Result.success()
    } catch (exception: HttpException) {
        Result.retry()
    }
}