package com.twentiecker.storyapp.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.twentiecker.storyapp.api.ApiConfig
import com.twentiecker.storyapp.model.FileUploadResponse
import com.twentiecker.storyapp.model.UserModel
import com.twentiecker.storyapp.model.UserPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val pref: UserPreference) : ViewModel() {
    private val _message = MutableLiveData<String>()
    val messageData: LiveData<String> = _message

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun serviceUpload(token: String, imageMultipart: MultipartBody.Part, description: RequestBody, lat: Float, lon: Float) {
        val service =
            ApiConfig.getApiService().uploadImage(token, imageMultipart, description, lat, lon)
        service.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
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

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                _message.value = "Silakan masukkan berkas gambar terlebih dahulu."
            }
        })
    }
}