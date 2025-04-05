package com.zrifapps.storyapp.presentation.screens.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.zrifapps.storyapp.common.network.NetworkResult
import com.zrifapps.storyapp.data.story.request.GetStoryRequest
import com.zrifapps.storyapp.data.story.response.StoriesResponse
import com.zrifapps.storyapp.domain.story.entity.Story
import com.zrifapps.storyapp.domain.story.repository.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when get stories should return success with data`() = runTest {
        val dummyStories = listOf(
            Story(
                id = "story-1",
                name = "User 1",
                description = "Story 1 description",
                photoUrl = "https://story1.jpg",
                createdAt = "2023-01-01",
                lat = null,
                lon = null
            ),
            Story(
                id = "story-2",
                name = "User 2",
                description = "Story 2 description",
                photoUrl = "https://story2.jpg",
                createdAt = "2023-01-02",
                lat = null,
                lon = null
            ),
            Story(
                id = "story-3",
                name = "User 3",
                description = "Story 3 description",
                photoUrl = "https://story3.jpg",
                createdAt = "2023-01-03",
                lat = null,
                lon = null
            )
        )


        lenient().`when`(storyRepository.stories(GetStoryRequest(page = 1, location = 0)))
            .thenReturn(
                NetworkResult.Success(
                    StoriesResponse(
                        false,
                        "Stories fetched successfully",
                        dummyStories
                    )
                )
            )


        homeViewModel = HomeViewModel(storyRepository)

        val pagingData = PagingData.from(dummyStories)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryDiffCallback(),
            updateCallback = noopListUpdateCallback,
            workerDispatcher = UnconfinedTestDispatcher(testScheduler)
        )

        differ.submitData(pagingData)

        val actualStories = differ.snapshot().items

        assertNotNull(actualStories)
        assertEquals(dummyStories.size, actualStories.size)
        assertEquals(dummyStories[0], actualStories[0])

    }

    @Test
    fun `when get stories should return empty list`() = runTest {
        val emptyList = emptyList<Story>()

        lenient().`when`(storyRepository.stories(GetStoryRequest(page = 1, location = 0)))
            .thenReturn(
                NetworkResult.Success(
                    StoriesResponse(
                        error = false,
                        message = "No stories found",
                        listStory = emptyList()
                    )
                )
            )

        homeViewModel = HomeViewModel(storyRepository)

        val pagingData = PagingData.from(emptyList)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryDiffCallback(),
            updateCallback = noopListUpdateCallback,
            workerDispatcher = UnconfinedTestDispatcher(testScheduler)
        )

        differ.submitData(pagingData)

        val actualStories = differ.snapshot().items

        assertEquals(0, actualStories.size)
    }


    class TestStoryPagingSource(private val data: List<Story>) : PagingSource<Int, Story>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
            return LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = if (data.isNotEmpty()) 2 else null
            )
        }

        override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }
    }

    class StoryDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem == newItem
        }
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}
