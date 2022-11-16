package com.twentiecker.storyapp.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.twentiecker.storyapp.api.ApiResult
import com.twentiecker.storyapp.model.FileUploadResponse
import com.twentiecker.storyapp.model.UserModel
import com.twentiecker.storyapp.model.UserPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private val pref: UserPreference,
    private val addStoryRepository: AddStoryRepository
) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun serviceUpload(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        lat: Float,
        lon: Float
    ): LiveData<ApiResult<FileUploadResponse>> =
        addStoryRepository.serviceUpload(token, imageMultipart, description, lat, lon)
}