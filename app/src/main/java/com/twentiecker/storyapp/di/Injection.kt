package com.twentiecker.storyapp.di

import com.twentiecker.storyapp.addstory.AddStoryRepository
import com.twentiecker.storyapp.api.ApiConfig
import com.twentiecker.storyapp.authentication.login.LoginRepository
import com.twentiecker.storyapp.authentication.register.RegisterRepository
import com.twentiecker.storyapp.data.ListStoryRepository
import com.twentiecker.storyapp.maps.MapsRepository

object Injection {

    fun provideRepository(): ListStoryRepository {
        val apiService = ApiConfig.getApiService()
        return ListStoryRepository(apiService)
    }

    fun loginRepository(): LoginRepository {
        val apiService = ApiConfig.getApiService()
        return LoginRepository(apiService)
    }

    fun registerRepository(): RegisterRepository {
        val apiService = ApiConfig.getApiService()
        return RegisterRepository(apiService)
    }

    fun addStoryRepository(): AddStoryRepository {
        val apiService = ApiConfig.getApiService()
        return AddStoryRepository(apiService)
    }

    fun mapsRepository(): MapsRepository {
        val apiService = ApiConfig.getApiService()
        return MapsRepository(apiService)
    }
}