package com.twentiecker.storyapp.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.twentiecker.storyapp.api.ApiResult
import com.twentiecker.storyapp.api.ApiService
import com.twentiecker.storyapp.utils.DataDummy
import com.twentiecker.storyapp.utils.FakeApiService
import com.twentiecker.storyapp.utils.MainDispatcherRule
import com.twentiecker.storyapp.utils.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MapsRepositoryTest {
    private lateinit var apiService: ApiService
    private lateinit var mapsRepository: MapsRepository
    private var token: String = "token"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        apiService = FakeApiService()
        mapsRepository = MapsRepository(apiService)
    }

    @Test
    fun `when successfully get all maps stories`() = runTest {
        val expectedMaps = DataDummy.generateDummyMapsResponse()
        val actualMaps = mapsRepository.listMaps(token)
        actualMaps.observeForTesting {
            Assert.assertNotNull(actualMaps)  // Memastikan data tidak null.
            Assert.assertEquals(
                expectedMaps.listStory, (actualMaps.value as ApiResult.Success).data.listStory
            ) // Memastikan data sesuai dengan yang diharapkan.
        }
    }
}