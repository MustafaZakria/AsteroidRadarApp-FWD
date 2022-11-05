package com.udacity.asteroidradar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.model.Asteroid

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asteroids: List<Asteroid>)

    @Query("SELECT * FROM Asteroid_table WHERE DATE(close_approach_date) <= DATE(:endDate)")
    fun getAsteroidsByDate(endDate: String): LiveData<List<Asteroid>>

}