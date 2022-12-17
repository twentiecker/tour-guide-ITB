package com.twentiecker.storyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.twentiecker.storyapp.addstory.AddStoryViewModel
import com.twentiecker.storyapp.authentication.login.LoginViewModel
import com.twentiecker.storyapp.authentication.register.RegisterViewModel
import com.twentiecker.storyapp.di.Injection
import com.twentiecker.storyapp.liststory.ListStoryViewModel
import com.twentiecker.storyapp.maps.MapsViewModel
import com.twentiecker.storyapp.model.UserPreference

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(ListStoryViewModel::class.java) -> {
                ListStoryViewModel(pref, Injection.provideRepository()) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(Injection.registerRepository()) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref, Injection.loginRepository()) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(pref, Injection.addStoryRepository()) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(pref, Injection.mapsRepository()) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}