package com.zrifapps.storyapp.data.story.paging


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zrifapps.storyapp.common.network.NetworkResult
import com.zrifapps.storyapp.data.story.request.GetStoryRequest
import com.zrifapps.storyapp.domain.story.entity.Story
import com.zrifapps.storyapp.domain.story.repository.StoryRepository

class StoryPagingSource(
    private val storyRepository: StoryRepository,
) : PagingSource<Int, Story>() {

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: 1

            when (val result = storyRepository.stories(GetStoryRequest(page = page))) {
                is NetworkResult.Success -> {
                    val stories = result.data.listStory.map { storyDto ->
                        Story(
                            id = storyDto.id,
                            name = storyDto.name,
                            description = storyDto.description,
                            photoUrl = storyDto.photoUrl,
                            createdAt = storyDto.createdAt,
                            lat = storyDto.lat,
                            lon = storyDto.lon
                        )
                    }

                    val nextKey = if (stories.isEmpty()) null else page + 1
                    LoadResult.Page(
                        data = stories,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = nextKey
                    )
                }

                is NetworkResult.Error -> {
                    LoadResult.Error(Exception(result.message ?: "Unknown error occurred"))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
