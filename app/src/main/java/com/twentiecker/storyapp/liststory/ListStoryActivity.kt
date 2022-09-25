package com.twentiecker.storyapp.liststory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.twentiecker.storyapp.R
import com.twentiecker.storyapp.authentication.register.RegisterConfig
import com.twentiecker.storyapp.authentication.register.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListStoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_story)

        val service = ListStoryConfig.getListStoryService().getListStory(1)
        service.enqueue(object : Callback<ListStoryResponse> {
            override fun onResponse(
                call: Call<ListStoryResponse>,
                response: Response<ListStoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Toast.makeText(this@ListStoryActivity, responseBody.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this@ListStoryActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                Toast.makeText(
                    this@ListStoryActivity,
                    "Id tidak ditemukan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}