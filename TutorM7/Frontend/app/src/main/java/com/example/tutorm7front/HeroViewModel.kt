package com.example.tutorm7front

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutorm7front.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeroViewModel : ViewModel() {

    private val _heroes = MutableLiveData<List<HeroEntity>>()
    val heroes: LiveData<List<HeroEntity>> = _heroes

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.instance.getHeroes()
                withContext(Dispatchers.Main) {
                    _heroes.value = response.heroes
                    _error.value = null // Clear previous errors
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _error.value = e.message
                }
            }
        }
    }
}
