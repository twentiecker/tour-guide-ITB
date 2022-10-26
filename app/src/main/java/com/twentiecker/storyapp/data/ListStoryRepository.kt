package com.twentiecker.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.twentiecker.storyapp.api.ApiService
import com.twentiecker.storyapp.database.ListStoryDatabase
import com.twentiecker.storyapp.model.ListStoryItem

class ListStoryRepository(
    private val quoteDatabase: ListStoryDatabase,
    private val apiService: ApiService
) {
    fun getStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                ListStoryPagingSource(apiService, token)
            }
        ).liveData
    }
}