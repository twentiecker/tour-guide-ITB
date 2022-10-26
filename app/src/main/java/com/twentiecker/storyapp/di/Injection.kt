package com.twentiecker.storyapp.di

import android.content.Context
import com.twentiecker.storyapp.api.ApiConfig
import com.twentiecker.storyapp.data.ListStoryRepository
import com.twentiecker.storyapp.database.ListStoryDatabase

object Injection {
    fun provideRepository(context: Context): ListStoryRepository {
        val database = ListStoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return ListStoryRepository(database, apiService)
    }
}