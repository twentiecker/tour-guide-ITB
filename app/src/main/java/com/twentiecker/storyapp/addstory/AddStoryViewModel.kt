package com.twentiecker.storyapp.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.twentiecker.storyapp.model.UserModel
import com.twentiecker.storyapp.model.UserPreference

class AddStoryViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}