package com.twentiecker.storyapp.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.twentiecker.storyapp.api.ApiService
import com.twentiecker.storyapp.api.ApiResult
import com.twentiecker.storyapp.model.LoginResponse

class LoginRepository(private val apiService: ApiService) {

    fun loginService(email: String, pass: String): LiveData<ApiResult<LoginResponse>> = liveData {
        try {
            emit(ApiResult.Loading)
            val response = apiService.loginUser(email, pass)
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