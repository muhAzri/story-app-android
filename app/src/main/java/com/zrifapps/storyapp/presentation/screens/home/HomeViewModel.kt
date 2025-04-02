package com.zrifapps.storyapp.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zrifapps.storyapp.data.story.paging.StoryPagingSource
import com.zrifapps.storyapp.domain.story.entity.Story
import com.zrifapps.storyapp.domain.story.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _storiesPagingFlow = MutableStateFlow<PagingData<Story>>(PagingData.empty())
    val storiesPagingFlow: StateFlow<PagingData<Story>> = _storiesPagingFlow

    init {
        loadStories()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.RefreshStories -> {
                loadStories()
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

    private fun loadStories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val pagingData = Pager(
                    config = PagingConfig(
                        pageSize = 10,
                        enablePlaceholders = false,
                        prefetchDistance = 2
                    ),
                    pagingSourceFactory = { StoryPagingSource(storyRepository) }
                ).flow.cachedIn(viewModelScope)

                pagingData.collect {
                    _storiesPagingFlow.value = it
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Unknown error occurred",
                    isLoading = false
                )
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(e.message ?: "Unknown error occurred")
                )
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class NavigateToStoryDetail(val storyId: String) : UiEvent()
        data object NavigateToAddStory : UiEvent()
    }
}
