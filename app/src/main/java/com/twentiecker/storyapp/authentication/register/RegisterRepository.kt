package com.twentiecker.storyapp.authentication.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.twentiecker.storyapp.api.ApiResult
import com.twentiecker.storyapp.api.ApiService
import com.twentiecker.storyapp.model.RegisterResponse

class RegisterRepository(private val apiService: ApiService) {

    fun registerService(
        name: String,
        email: String,
        pass: String
    ): LiveData<ApiResult<RegisterResponse>> = liveData {
        try {
            emit(ApiResult.Loading)
            val response = apiService.registerUser(name, email, pass)
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