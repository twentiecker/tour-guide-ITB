package com.twentiecker.storyapp.authentication.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.twentiecker.storyapp.api.ApiResult
import com.twentiecker.storyapp.model.RegisterResponse

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    fun registerService(
        name: String,
        email: String,
        pass: String
    ): LiveData<ApiResult<RegisterResponse>> =
        registerRepository.registerService(name, email, pass)
}