package com.twentiecker.storyapp.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.twentiecker.storyapp.api.ApiResult
import com.twentiecker.storyapp.api.ApiService
import com.twentiecker.storyapp.utils.DataDummy
import com.twentiecker.storyapp.utils.FakeApiService
import com.twentiecker.storyapp.utils.MainDispatcherRule
import com.twentiecker.storyapp.utils.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AddStoryRepositoryTest {
    private lateinit var apiService: ApiService
    private lateinit var addStoryRepository: AddStoryRepository
    private var token: String = "token"
    private val multipartBody = MultipartBody.Part.createFormData(
        "photo",
        "image-name",
        "image-file".toRequestBody()
    )
    private val toRequestBody = "description".toRequestBody()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        apiService = FakeApiService()
        addStoryRepository = AddStoryRepository(apiService)
    }

    @Test
    fun `when Get AddStoryResponse Should Not Null`() = runTest {
        val expectedLogin = DataDummy.generateDummyAddStoryResponse()
        val actualLogin =
            addStoryRepository.serviceUpload(token, multipartBody, toRequestBody, 0.0f, 0.0f)
        actualLogin.observeForTesting {
            Assert.assertNotNull(actualLogin)  // Memastikan data tidak null.
            Assert.assertEquals(
                expectedLogin, (actualLogin.value as ApiResult.Success).data
            ) // Memastikan data sesuai dengan yang diharapkan.
        }
    }
}