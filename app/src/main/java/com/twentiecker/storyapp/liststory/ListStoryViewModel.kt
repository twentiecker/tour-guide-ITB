package com.twentiecker.storyapp.liststory

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.twentiecker.storyapp.data.ListStoryRepository
import com.twentiecker.storyapp.model.ListStoryItem
import com.twentiecker.storyapp.model.UserModel
import com.twentiecker.storyapp.model.UserPreference
import kotlinx.coroutines.launch

class ListStoryViewModel(
    private val pref: UserPreference,
    private val listStoryRepository: ListStoryRepository
) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun listStory(token: String): LiveData<PagingData<ListStoryItem>> =
        listStoryRepository.getStory(token).cachedIn(viewModelScope)
}