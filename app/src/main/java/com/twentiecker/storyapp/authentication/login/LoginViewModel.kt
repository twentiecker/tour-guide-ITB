package com.twentiecker.storyapp.authentication.login

import androidx.lifecycle.*
import com.twentiecker.storyapp.api.ApiResult
import com.twentiecker.storyapp.model.*
import kotlinx.coroutines.launch

class LoginViewModel(
    private val pref: UserPreference,
    private val loginRepository: LoginRepository
) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

//    fun login() {
//        viewModelScope.launch {
//            pref.login()
//        }
//    }

    fun loginService(email: String, pass: String): LiveData<ApiResult<LoginResponse>> =
        loginRepository.loginService(email, pass)
}