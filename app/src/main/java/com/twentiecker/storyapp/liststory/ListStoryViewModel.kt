package com.twentiecker.storyapp.liststory

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.twentiecker.storyapp.model.UserModel
import com.twentiecker.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    // retrofitnya
    fun listStory(token: String) {
        val service = ListStoryConfig.getListStoryService().getListStory(token, 1)
        service.enqueue(object : Callback<ListStoryResponse> {
            override fun onResponse(
                call: Call<ListStoryResponse>,
                response: Response<ListStoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _listStory.value = responseBody.listStory
                        Log.v("liststory", "sukses ambil data")
//                        showRecyclerList(responseBody.listStory)
//                        Toast.makeText(this@ListStoryActivity, responseBody.message, Toast.LENGTH_SHORT)
//                            .show()
                    }
                } else {
//                    Toast.makeText(this@ListStoryActivity, response.message(), Toast.LENGTH_SHORT)
//                        .show()
                    Log.e("liststory", "gagal ambil data")
                }
            }

            override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
//                Toast.makeText(
//                    this@ListStoryActivity,
//                    "Id tidak ditemukan",
//                    Toast.LENGTH_SHORT
//                ).show()
                Log.e("liststory", "gak ada apa-apa")
            }
        })
    }

}