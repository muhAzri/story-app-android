package com.zrifapps.storyapp.presentation.screens.map

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
class MapViewViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
) : ViewModel() {

    var state by mutableStateOf(MapViewState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: MapViewEvent) {
        when (event) {
            is MapViewEvent.LoadStoriesWithLocation -> {
                getStoriesWithLocation()
            }
            //

            is MapViewEvent.NavigateToStoryDetail -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateToStoryDetail(event.storyId))
                }
            }
        }
    }

    private fun getStoriesWithLocation() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            when (val result = storyRepository.stories(GetStoryRequest(page = 1, location = 1))) {
                is NetworkResult.Success -> {
                    val stories = result.data.listStory.mapNotNull { storyDto ->
                        if (storyDto.lat != null && storyDto.lon != null) {
                            Story(
                                id = storyDto.id,
                                name = storyDto.name,
                                description = storyDto.description,
                                photoUrl = storyDto.photoUrl,
                                createdAt = storyDto.createdAt,
                                lat = storyDto.lat,
                                lon = storyDto.lon
                            )
                        } else null
                    }

                    state = state.copy(
                        stories = stories,
                        isLoading = false,
                        error = null
                    )
                }

                is NetworkResult.Error -> {
                    state = state.copy(
                        error = result.message ?: "Unknown error occurred",
                        isLoading = false
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
    }
}
