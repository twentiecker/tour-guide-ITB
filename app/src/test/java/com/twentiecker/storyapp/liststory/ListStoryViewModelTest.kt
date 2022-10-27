package com.twentiecker.storyapp.liststory

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.twentiecker.storyapp.data.ListStoryRepository
import com.twentiecker.storyapp.model.ListStoryItem
import com.twentiecker.storyapp.model.UserPreference
import com.twentiecker.storyapp.utils.DataDummy
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ListStoryViewModelTest {
    @Mock
    private lateinit var listStoryRepository: ListStoryRepository //newsRepository : NewsRepository
    private lateinit var listStoryViewModel: ListStoryViewModel //newsViewModel: NewsViewModel
    private val dummyNews = DataDummy.generateDummyStoryList()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

//    @Before
//    fun setUp() {
//        listStoryViewModel = ListStoryViewModel(UserPreference.getInstance(), listStoryRepository)
//        //newsViewModel = NewsViewModel(newsRepository)
//    }
//
//    @Test
//    fun `when Get ListStory Should Not Null and Return Success`() {
//        val expectedStory = LiveData<<List<ListStoryItem>>()
//        expectedStory.value = dummyNews
//        `when`(listStoryRepository.getStory("token")).thenReturn(expectedStory)
//        val actualNews = listStoryViewModel.listStory()
//        Assert.assertNotNull(actualNews)
//    }
}