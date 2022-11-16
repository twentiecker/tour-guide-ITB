package com.twentiecker.storyapp.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.twentiecker.storyapp.api.ApiResult
import com.twentiecker.storyapp.model.ListStoryResponse
import com.twentiecker.storyapp.model.UserPreference
import com.twentiecker.storyapp.utils.DataDummy
import com.twentiecker.storyapp.utils.getOrAwaitValue
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
class MapsViewModelTest {
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyMapsResponse = DataDummy.generateDummyMapsResponse()
    private var token: String = "token"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userPreference: UserPreference

    @Mock
    private lateinit var mapsRepository: MapsRepository

    @Before
    fun setup() {
        mapsViewModel = MapsViewModel(userPreference, mapsRepository)
    }

    @Test
    fun `when get maps stories should not null and return success`() {
        val expectedMaps = MutableLiveData<ApiResult<ListStoryResponse>>()
        expectedMaps.value = ApiResult.Success(dummyMapsResponse)
        `when`(mapsRepository.listMaps(token)).thenReturn(expectedMaps)

        val actualMaps = mapsViewModel.listMaps(token).getOrAwaitValue()

        Mockito.verify(mapsRepository).listMaps(token)
        assertNotNull(actualMaps)  // Memastikan data tidak null.
        assertTrue(actualMaps is ApiResult.Success)   // Memastikan mengembalikan Result.Success.
        assertEquals(
            dummyMapsResponse.listStory, (actualMaps as ApiResult.Success).data.listStory
        )  // Memastikan data sesuai dengan yang diharapkan.
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedMaps = MutableLiveData<ApiResult<ListStoryResponse>>()
        expectedMaps.value = ApiResult.Error("Error")
        `when`(mapsRepository.listMaps(token)).thenReturn(expectedMaps)

        val actualMaps = mapsViewModel.listMaps(token).getOrAwaitValue()

        Mockito.verify(mapsRepository).listMaps(token)
        assertNotNull(actualMaps)  // Memastikan data tidak null.
        assertTrue(actualMaps is ApiResult.Error)   // Memastikan mengembalikan Result.Error.
    }
}