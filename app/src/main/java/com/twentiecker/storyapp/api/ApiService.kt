package com.twentiecker.storyapp.api

import com.twentiecker.storyapp.model.ListStoryResponse
import com.twentiecker.storyapp.model.RegisterResponse
import com.twentiecker.storyapp.model.LoginResponse
import com.twentiecker.storyapp.model.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<FileUploadResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("stories")
    fun getListStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Call<ListStoryResponse>
}