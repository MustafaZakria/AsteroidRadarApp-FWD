package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.utils.Constants.API_KEY
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidApiService {
    @GET("/neo/rest/v1/feed")
    suspend fun getAsteroids(@Query("api_key")apiKey: String = API_KEY, @Query("start_date")startDate: String): Response<String>

    @GET("/planetary/apod")
    suspend fun getPictureOfDay(@Query("api_key")apiKey: String = API_KEY): Response<PictureOfDay>

    companion object {

        val retrofitServiceMoshi: AsteroidApiService by lazy {
            RetrofitClient.getMoshiInstance().create(AsteroidApiService::class.java)
        }

        val retrofitServiceScalar: AsteroidApiService by lazy {
            RetrofitClient.getScalarInstance().create(AsteroidApiService::class.java)
        }


    }
}