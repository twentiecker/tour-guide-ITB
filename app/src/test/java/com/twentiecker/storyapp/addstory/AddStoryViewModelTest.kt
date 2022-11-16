package com.twentiecker.storyapp.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.twentiecker.storyapp.api.ApiResult
import com.twentiecker.storyapp.model.FileUploadResponse
import com.twentiecker.storyapp.model.UserPreference
import com.twentiecker.storyapp.utils.DataDummy
import com.twentiecker.storyapp.utils.getOrAwaitValue
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {
    private lateinit var addStoryViewModel: AddStoryViewModel
    private val dummyAddStoryResponse = DataDummy.generateDummyAddStoryResponse()
    private var token: String = "token"
    private val multipartBody = MultipartBody.Part.createFormData(
        "photo",
        "image-name",
        "image-file".toRequestBody()
    )
    private val toRequestBody = "description".toRequestBody()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userPreference: UserPreference

    @Mock
    private lateinit var addStoryRepository: AddStoryRepository

    @Before
    fun setup() {
        addStoryViewModel = AddStoryViewModel(userPreference, addStoryRepository)
    }

    @Test
    fun `when Get AddStoryResponse Should Not Null and Return Success`() {
        val expectedAddStory = MutableLiveData<ApiResult<FileUploadResponse>>()
        expectedAddStory.value = ApiResult.Success(dummyAddStoryResponse)
        `when`(
            addStoryRepository.serviceUpload(token, multipartBody, toRequestBody, 0.0f, 0.0f)
        ).thenReturn(expectedAddStory)

        val actualAddStory =
            addStoryViewModel.serviceUpload(token, multipartBody, toRequestBody, 0.0f, 0.0f)
                .getOrAwaitValue()

        Mockito.verify(addStoryRepository)
            .serviceUpload(token, multipartBody, toRequestBody, 0.0f, 0.0f)
        assertNotNull(actualAddStory)  // Memastikan data tidak null.
        assertTrue(actualAddStory is ApiResult.Success)   // Memastikan mengembalikan Result.Success.
        assertEquals(
            dummyAddStoryResponse, (actualAddStory as ApiResult.Success).data
        )  // Memastikan data sesuai dengan yang diharapkan.
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedAddStory = MutableLiveData<ApiResult<FileUploadResponse>>()
        expectedAddStory.value = ApiResult.Error("Error")
        `when`(
            addStoryRepository.serviceUpload(token, multipartBody, toRequestBody, 0.0f, 0.0f)
        ).thenReturn(expectedAddStory)

        val actualAddStory =
            addStoryViewModel.serviceUpload(token, multipartBody, toRequestBody, 0.0f, 0.0f)
                .getOrAwaitValue()

        Mockito.verify(addStoryRepository)
            .serviceUpload(token, multipartBody, toRequestBody, 0.0f, 0.0f)
        assertNotNull(actualAddStory)  // Memastikan data tidak null.
        assertTrue(actualAddStory is ApiResult.Error)   // Memastikan mengembalikan Result.Error.
    }
}