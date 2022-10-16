package com.twentiecker.storyapp.authentication.login

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.twentiecker.storyapp.model.UserModel
import com.twentiecker.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {
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

    private val _userData = MutableLiveData<DataItem>()
    val userData: LiveData<DataItem> = _userData

    fun loginService(email: String, pass: String) {
        val service = LoginConfig().getLoginService().loginUser(email, pass)
        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
//                        Toast.makeText(
//                            this@LoginActivity,
//                            responseBody.message,
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
                        _userData.value = responseBody.loginResult
                        Log.v("userData", responseBody.loginResult.toString())
//                        saveUser(
//                            UserModel(
//                                userData.value?.userId ?:"" ,
//                                userData.value?.name ?: "",
//                                userData.value?.token ?: "",
//                                true
//                            )
//                        )
//                        login()
                    }
                } else {
//                    Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_SHORT)
//                        .show()
                    Log.v("userData", "gagal ambil data")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Toast.makeText(
//                    this@LoginActivity,
//                    "Id tidak ditemukan",
//                    Toast.LENGTH_SHORT
//                ).show()
                Log.v("userData", "id tidak ditemukan")
            }
        })

//            Toast.makeText(
//                this@LoginActivity,
//                "${edLoginEmail.text} and ${edLoginPassword.text}",
//                Toast.LENGTH_SHORT
//            ).show()
    }
}