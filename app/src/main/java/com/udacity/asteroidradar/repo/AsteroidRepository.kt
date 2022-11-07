package com.udacity.asteroidradar.repo

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.api.AsteroidApiService.Companion.retrofitServiceMoshi
import com.udacity.asteroidradar.api.AsteroidApiService.Companion.retrofitServiceScalar
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.db.AsteroidDao
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.AsteroidType
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val asteroidDao: AsteroidDao) {

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("yyyy-MM-dd")

    fun getAsteroids(type: AsteroidType = AsteroidType.SAVED): LiveData<List<Asteroid>> = when(type) {
        AsteroidType.TODAY -> asteroidDao.getAsteroidsToday(formatter.format(Date()))
        else -> asteroidDao.getAsteroidsWeek(formatter.format(Date()))
        //else -> asteroidDao.getAllAsteroids()
    }

    suspend fun refreshAsteroids() {

        try {
            val current = formatter.format(Date())
            val response = retrofitServiceScalar.getAsteroids(startDate = current)
            if (response.isSuccessful) {
                val result = parseAsteroidsJsonResult(JSONObject(response.body().toString()))

                withContext(Dispatchers.IO) {
                    asteroidDao.insert(result)
                }
            }
        } catch (exception: Exception) {
            Log.e("Repository", exception.message.toString())
        }
    }

    suspend fun getImageOfDay(): PictureOfDay? {
        try {
            val response = retrofitServiceMoshi.getPictureOfDay()
            if (response.isSuccessful) {
                val picOfDay = response.body()
                picOfDay?.let {
                    if (it.mediaType == "image")
                        return it
                }
            }
        } catch (exception: Exception) {
            Log.e("Repository", exception.message.toString())
        }
        return null
    }

}