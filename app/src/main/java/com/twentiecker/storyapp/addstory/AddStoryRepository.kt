package com.twentiecker.storyapp.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.twentiecker.storyapp.api.ApiResult
import com.twentiecker.storyapp.api.ApiService
import com.twentiecker.storyapp.model.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryRepository(private val apiService: ApiService) {

    fun serviceUpload(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        lat: Float,
        lon: Float
    ): LiveData<ApiResult<FileUploadResponse>> = liveData {
        try {
            emit(ApiResult.Loading)
            val response = apiService.uploadImage(token, imageMultipart, description, lat, lon)
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