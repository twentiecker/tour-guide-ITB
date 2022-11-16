package com.twentiecker.storyapp.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.twentiecker.storyapp.api.ApiResult
import com.twentiecker.storyapp.api.ApiService
import com.twentiecker.storyapp.model.ListStoryResponse

class MapsRepository(private val apiService: ApiService) {

    fun listMaps(token: String): LiveData<ApiResult<ListStoryResponse>> = liveData {
        try {
            emit(ApiResult.Loading)
            val response = apiService.getListMaps(token, 1, 1)
            if (!response.error) {
                emit(ApiResult.Success(response))
            } else {
                emit(ApiResult.Error(response.message))
            }
        } catch (ex: Exception) {
            emit(ApiResult.Error(ex.message.toString()))
        }
    }
}