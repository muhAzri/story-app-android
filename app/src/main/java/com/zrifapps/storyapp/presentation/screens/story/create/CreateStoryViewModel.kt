package com.zrifapps.storyapp.presentation.screens.story.create

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zrifapps.storyapp.R
import com.zrifapps.storyapp.common.network.NetworkResult
import com.zrifapps.storyapp.common.util.FileUtils
import com.zrifapps.storyapp.data.story.request.CreateStoryRequest
import com.zrifapps.storyapp.domain.story.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

@HiltViewModel
class CreateStoryViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    var state by mutableStateOf(CreateStoryState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: CreateStoryEvent) {
        when (event) {
            is CreateStoryEvent.SetPhotoUri -> {
                setPhotoUri(event.uri)
            }

            is CreateStoryEvent.CreateStory -> {
                createStory(event.description, event.lat, event.lon)
            }

            is CreateStoryEvent.ClearError -> {
                clearError()
            }
        }
    }

    private fun setPhotoUri(uri: Uri) {
        state = state.copy(photoUri = uri)
    }

    private fun clearError() {
        state = state.copy(error = null)
    }

    private fun createStory(description: String, lat: Float?, lon: Float?) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            try {
                val request = CreateStoryRequest(
                    description = description,
                    lat = lat,
                    lon = lon
                )

                val photoUri = state.photoUri ?: run {
                    state = state.copy(
                        error = context.getString(R.string.photo_required), isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowSnackbar(context.getString(R.string.photo_required)))
                    return@launch
                }

                val photoFile = FileUtils.uriToFile(photoUri, context)
                val requestImageFile = photoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val photoPart = MultipartBody.Part.createFormData(
                    "photo", photoFile.name, requestImageFile
                )

                when (val result = storyRepository.createStory(request, photoPart)) {
                    is NetworkResult.Success -> {
                        state = state.copy(
                            isLoading = false, isSuccess = true
                        )
                        _eventFlow.emit(UiEvent.NavigateBack)
                    }

                    is NetworkResult.Error -> {
                        val errorMessage = result.message ?: "Unknown error occurred"
                        state = state.copy(
                            isLoading = false, error = errorMessage
                        )
                        _eventFlow.emit(UiEvent.ShowSnackbar(errorMessage))
                    }
                }
            } catch (e: Exception) {
                val errorMessage = "Error: ${e.message}"
                state = state.copy(
                    isLoading = false, error = errorMessage
                )
                _eventFlow.emit(UiEvent.ShowSnackbar(errorMessage))
            }
        }
    }


    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object NavigateBack : UiEvent()
    }
}
