package com.twentiecker.storyapp.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.twentiecker.storyapp.model.BikeModel
import com.twentiecker.storyapp.model.UserModel
import com.twentiecker.storyapp.model.UserPreference
import kotlinx.coroutines.launch

class ScanViewModel(private var pref: UserPreference) : ViewModel() {
    fun getBike(): LiveData<BikeModel> {
        return pref.getBike().asLiveData()
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun scan() {
        viewModelScope.launch {
            pref.scan()
        }
    }
}