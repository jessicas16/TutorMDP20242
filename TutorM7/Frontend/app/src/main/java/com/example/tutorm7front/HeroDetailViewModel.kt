package com.example.tutorm7front

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutorm7front.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeroDetailViewModel : ViewModel() {

    private val _deleteResult = MutableLiveData<Boolean>()
    val deleteResult: LiveData<Boolean> = _deleteResult

    private val _editResult = MutableLiveData<Boolean>()
    val editResult: LiveData<Boolean> = _editResult

    private val _hero = MutableLiveData<HeroEntity>()
    val hero: LiveData<HeroEntity> = _hero

    fun fetchHeroById(heroId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.instance.getHeroById(heroId)
                withContext(Dispatchers.Main) {
                    _hero.value = response.hero
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun deleteHero(heroId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                RetrofitInstance.instance.deleteHero(heroId)
                withContext(Dispatchers.Main) {
                    _deleteResult.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _deleteResult.value = false
                }
            }
        }
    }

    fun updateHero(heroId: String, updatedHero: HeroEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                RetrofitInstance.instance.updateHero(heroId, updatedHero)
                withContext(Dispatchers.Main) {
                    _editResult.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _editResult.value = false
                }
            }
        }
    }
}
