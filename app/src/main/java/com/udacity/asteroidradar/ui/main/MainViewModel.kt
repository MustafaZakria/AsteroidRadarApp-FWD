package com.udacity.asteroidradar.ui.main

import androidx.lifecycle.*
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.AsteroidType
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.repo.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel constructor(private val repository: AsteroidRepository) : ViewModel() {

    private var type = MutableLiveData(AsteroidType.WEEK)

    val asteroids: LiveData<List<Asteroid>> = Transformations.switchMap(type) { type ->
        when (type) {
            AsteroidType.TODAY -> repository.getAsteroids(AsteroidType.TODAY )
            AsteroidType.WEEK -> repository.getAsteroids(AsteroidType.WEEK )
            AsteroidType.SAVED -> repository.getAsteroids(AsteroidType.SAVED )
        }
    }

    private val _pictureOfDay = MutableLiveData<PictureOfDay?>()
    val pictureOfDay: LiveData<PictureOfDay?>
        get() = _pictureOfDay


    init {
        refreshAsteroids()
        getPictureOFDay()
    }

    fun setType(currType: AsteroidType) {
        type.value = currType
    }

    private fun refreshAsteroids() {
        viewModelScope.launch {
            repository.refreshAsteroids()
        }
    }

    private fun getPictureOFDay() {
        viewModelScope.launch {
            repository.getImageOfDay()?.let {
                _pictureOfDay.value = it
            }
        }
    }

    private val _navigateToAsteroidDetail = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetail: LiveData<Asteroid?>
        get() = _navigateToAsteroidDetail

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroidDetail.value = asteroid
    }

    fun onAsteroidDetailNavigated() {
        _navigateToAsteroidDetail.value = null
    }

}

class MainViewModelFactory(private val repository: AsteroidRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}