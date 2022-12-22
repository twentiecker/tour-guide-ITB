package com.twentiecker.storyapp.bike

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.twentiecker.storyapp.model.BikeModel
import com.twentiecker.storyapp.model.UserModel
import com.twentiecker.storyapp.model.UserPreference
import kotlinx.coroutines.launch

class BikeViewModel(private val pref: UserPreference) : ViewModel() {
    fun saveBike(bike: BikeModel) {
        viewModelScope.launch {
            pref.saveBike(bike)
        }
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getBike(): LiveData<BikeModel> {
        return pref.getBike().asLiveData()
    }
}