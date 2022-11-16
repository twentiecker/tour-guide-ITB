package com.twentiecker.storyapp.utils

import com.twentiecker.storyapp.api.ApiService
import com.twentiecker.storyapp.model.FileUploadResponse
import com.twentiecker.storyapp.model.ListStoryResponse
import com.twentiecker.storyapp.model.LoginResponse
import com.twentiecker.storyapp.model.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {
    private val dummyAddStoryResponse = DataDummy.generateDummyAddStoryResponse()
    override suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Float,
        lon: Float
    ): FileUploadResponse {
        return dummyAddStoryResponse
    }

    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    override suspend fun loginUser(email: String, password: String): LoginResponse {
        return dummyLoginResponse
    }

    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    override suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): RegisterResponse {
        return dummyRegisterResponse
    }

    private val dummyStoryResponse = DataDummy.generateDummyStoryResponse()
    override suspend fun getListStory(token: String, page: Int, size: Int): ListStoryResponse {
        return dummyStoryResponse
    }

    private val dummyMapsResponse = DataDummy.generateDummyMapsResponse()
    override suspend fun getListMaps(token: String, page: Int, location: Int): ListStoryResponse {
        return dummyMapsResponse
    }
}