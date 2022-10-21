package com.twentiecker.storyapp.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.twentiecker.storyapp.api.ApiConfig
import com.twentiecker.storyapp.model.ListStoryItem
import com.twentiecker.storyapp.model.ListStoryResponse
import com.twentiecker.storyapp.model.UserModel
import com.twentiecker.storyapp.model.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val pref: UserPreference) : ViewModel() {
    private val _message = MutableLiveData<String>()
    val messageData: LiveData<String> = _message
    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun listMaps(token: String) {
        val service = ApiConfig.getApiService().getListMaps(token, 1, 1)
        service.enqueue(object : Callback<ListStoryResponse> {
            override fun onResponse(
                call: Call<ListStoryResponse>,
                response: Response<ListStoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _listStory.value = responseBody.listStory
                        _message.value = responseBody.message
                    }
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                _message.value = "Id tidak ditemukan"
            }
        })
    }
}