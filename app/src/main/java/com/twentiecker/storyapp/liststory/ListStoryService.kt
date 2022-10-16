package com.twentiecker.storyapp.liststory

import com.twentiecker.storyapp.authentication.register.RegisterResponse
import retrofit2.Call
import retrofit2.http.*

interface ListStoryService {
//    @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUQ2bkZoNlE4MlBiT21FRk0iLCJpYXQiOjE2NjM5MDc2NTF9.7m436ENApR4-72l5-VeRd-WHxR3YagvHiuGhyFVNc8s")
    @GET("stories")
    fun getListStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Call<ListStoryResponse>
}