package com.zrifapps.storyapp.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zrifapps.storyapp.common.network.NetworkResult
import com.zrifapps.storyapp.data.story.request.GetStoryRequest
import com.zrifapps.storyapp.domain.story.entity.Story
import com.zrifapps.storyapp.domain.story.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentPage = 1
    private var isLastPage = false


    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.RefreshStories -> {
                getStories(false)
            }

            is HomeEvent.LoadMoreStories -> {
                if (!state.isLoadingMore && !isLastPage && !state.isLoading) {
                    getStories(true)
                }
            }

            is HomeEvent.NavigateToStoryDetail -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateToStoryDetail(event.storyId))
                }
            }

            is HomeEvent.NavigateToAddStory -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateToAddStory)
                }
            }
        }
    }

    private fun getStories(loadMore: Boolean) {
        viewModelScope.launch {

            if (!loadMore) {
                state = state.copy(isLoading = true, error = null)
                currentPage = 1
                isLastPage = false
            } else {
                state = state.copy(isLoadingMore = true, error = null)
            }

            when (val result = storyRepository.stories(GetStoryRequest(page = currentPage))) {
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

                    isLastPage = stories.isEmpty()

                    if (!loadMore) {
                        state = state.copy(
                            stories = stories,
                            isLoading = false,
                            isLoadingMore = false,
                            error = null
                        )
                        currentPage++
                    } else {
                        state = state.copy(
                            stories = state.stories + stories,
                            isLoadingMore = false,
                            error = null
                        )
                        currentPage++
                    }
                }

                is NetworkResult.Error -> {
                    state = state.copy(
                        error = result.message ?: "Unknown error occurred",
                        isLoading = false,
                        isLoadingMore = false
                    )
                    viewModelScope.launch {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                result.message ?: "Unknown error occurred"
                            )
                        )
                    }
                }

            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class NavigateToStoryDetail(val storyId: String) : UiEvent()
        data object NavigateToAddStory : UiEvent()
    }
}
