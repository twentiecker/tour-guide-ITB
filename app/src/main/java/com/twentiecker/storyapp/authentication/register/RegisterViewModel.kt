package com.twentiecker.storyapp.authentication.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twentiecker.storyapp.api.ApiConfig
import com.twentiecker.storyapp.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val _message = MutableLiveData<String>()
    val messageData: LiveData<String> = _message

    fun registerService(name: String, email: String, pass: String) {
        val service = ApiConfig.getApiService().registerUser(name, email, pass)
        service.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _message.value = responseBody.message
                    }
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _message.value = "Id tidak ditemukan"
            }
        })
    }
}