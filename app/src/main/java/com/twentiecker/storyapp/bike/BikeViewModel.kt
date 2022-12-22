package com.twentiecker.storyapp.bike

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twentiecker.storyapp.model.BikeModel
import com.twentiecker.storyapp.model.UserPreference
import kotlinx.coroutines.launch

class BikeViewModel(private val pref: UserPreference) : ViewModel() {
    fun saveBike(bike: BikeModel) {
        viewModelScope.launch {
            pref.saveBike(bike)
        }
    }
}