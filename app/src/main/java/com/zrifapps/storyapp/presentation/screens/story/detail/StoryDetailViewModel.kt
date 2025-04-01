package com.zrifapps.storyapp.presentation.screens.story.detail


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zrifapps.storyapp.R
import com.zrifapps.storyapp.common.network.NetworkResult
import com.zrifapps.storyapp.domain.story.entity.Story
import com.zrifapps.storyapp.domain.story.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryDetailViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    var state by mutableStateOf(StoryDetailState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: StoryDetailEvent) {
        when (event) {
            is StoryDetailEvent.LoadStory -> {
                getStoryDetail(event.storyId)
            }

            is StoryDetailEvent.ShareStory -> {
                shareStory()
            }

            is StoryDetailEvent.OpenMap -> {
                openMap()
            }
        }
    }

    private fun getStoryDetail(storyId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            when (val result = storyRepository.getStoryById(storyId)) {
                is NetworkResult.Success -> {
                    val storyDto = result.data.story
                    val story = Story(
                        id = storyDto.id,
                        name = storyDto.name,
                        description = storyDto.description,
                        photoUrl = storyDto.photoUrl,
                        createdAt = storyDto.createdAt,
                        lat = storyDto.lat,
                        lon = storyDto.lon
                    )

                    state = state.copy(
                        story = story, isLoading = false, error = null
                    )
                }

                is NetworkResult.Error -> {
                    state = state.copy(
                        error = result.message ?: "Unknown error occurred", isLoading = false
                    )
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            result.message ?: "Unknown error occurred"
                        )
                    )
                }
            }
        }
    }

    private fun shareStory() {
        val story = state.story ?: return

        val shareMessage = context.getString(
            R.string.share_story_message,
            story.name
        ) + "\n\n" + context.getString(
            R.string.share_story_description,
            story.description
        ) + "\n\n" + context.getString(R.string.shared_from_app)

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            type = "text/plain"
        }

        val intent =
            Intent.createChooser(shareIntent, context.getString(R.string.share_story_chooser_title))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


    private fun openMap() {
        val story = state.story ?: return

        if (story.lat == null || story.lon == null) {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowSnackbar(context.getString(R.string.location_information_is_unavailable)))
            }
            return
        }

        try {
            val uri =
                Uri.parse("geo:${story.lat},${story.lon}?q=${story.lat},${story.lon}(${story.name})")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.google.android.apps.maps")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowSnackbar(context.getString(R.string.unable_to_open_the_map)))
            }
        }


    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object NavigateBack : UiEvent()
    }
}



