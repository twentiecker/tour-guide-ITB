package com.twentiecker.storyapp.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.twentiecker.storyapp.api.ApiResult
import com.twentiecker.storyapp.model.ListStoryResponse
import com.twentiecker.storyapp.model.UserModel
import com.twentiecker.storyapp.model.UserPreference

class MapsViewModel(private val pref: UserPreference, private val mapsRepository: MapsRepository) :
    ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun listMaps(token: String): LiveData<ApiResult<ListStoryResponse>> =
        mapsRepository.listMaps(token)
}