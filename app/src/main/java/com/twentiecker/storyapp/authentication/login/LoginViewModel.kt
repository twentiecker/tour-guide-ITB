package com.twentiecker.storyapp.authentication.login

import androidx.lifecycle.*
import com.twentiecker.storyapp.api.ApiConfig
import com.twentiecker.storyapp.model.DataItem
import com.twentiecker.storyapp.model.LoginResponse
import com.twentiecker.storyapp.model.UserModel
import com.twentiecker.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {
    private val _userData = MutableLiveData<DataItem>()
    val userData: LiveData<DataItem> = _userData
    private val _message = MutableLiveData<String>()
    val messageData: LiveData<String> = _message

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }

    fun loginService(email: String, pass: String) {
        val service = ApiConfig.getApiService().loginUser(email, pass)
        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _message.value = responseBody.message
                        _userData.value = responseBody.loginResult
                    }
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _message.value = "Id tidak ditemukan"
            }
        })
    }
}