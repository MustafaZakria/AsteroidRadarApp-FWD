package com.udacity.asteroidradar

import android.app.Application
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.repo.AsteroidRepository

class AsteroidApplication : Application(){
    private val database by lazy { AsteroidDatabase.getInstance(this) }

    val repository by lazy { AsteroidRepository(database.dao) }
}