package com.twentiecker.storyapp.liststory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.twentiecker.storyapp.utils.DataDummy
import com.twentiecker.storyapp.utils.MainDispatcherRule
import com.twentiecker.storyapp.data.ListStoryRepository
import com.twentiecker.storyapp.utils.getOrAwaitValue
import com.twentiecker.storyapp.liststory.adapter.ListStoryAdapter
import com.twentiecker.storyapp.model.ListStoryItem
import com.twentiecker.storyapp.model.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListStoryViewModelTest {
    private var token: String = "token"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var listStoryRepository: ListStoryRepository

    @Mock
    private lateinit var preference: UserPreference

    @Test
    fun `when get stories should not null and return success`() = runTest {
        val dummyStory = DataDummy.generateDummyListStoryResponse()
        val data: PagingData<ListStoryItem> = ListStoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data
        `when`(listStoryRepository.getStory(token)).thenReturn(expectedStory)

        val listStoryViewModel = ListStoryViewModel(preference, listStoryRepository)
        val actualStory: PagingData<ListStoryItem> =
            listStoryViewModel.listStory(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory, differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].id, differ.snapshot()[0]?.id)
        assertEquals(dummyStory[0].photoUrl, differ.snapshot()[0]?.photoUrl)
        assertEquals(dummyStory[0].name, differ.snapshot()[0]?.name)
        assertEquals(dummyStory[0].description, differ.snapshot()[0]?.description)
        assertEquals(dummyStory[0].lat, differ.snapshot()[0]?.lat)
        assertEquals(dummyStory[0].lon, differ.snapshot()[0]?.lon)
        assertEquals(dummyStory[0].createdAt, differ.snapshot()[0]?.createdAt)
    }
}

class ListStoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}