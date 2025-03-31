package com.zrifapps.storyapp.presentation.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zrifapps.storyapp.common.network.NetworkResult
import com.zrifapps.storyapp.data.auth.request.RegisterRequest
import com.zrifapps.storyapp.domain.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Initial)
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(name: String, email: String, password: String) {
        _uiState.value = RegisterUiState.Loading
        viewModelScope.launch {
            val request = RegisterRequest(name, email, password)
            val result = authRepository.register(request)
            _uiState.value = when (result) {
                is NetworkResult.Success -> RegisterUiState.Success(
                    result.data.message
                )

                is NetworkResult.Error -> RegisterUiState.Error(result.message ?: "Unknown error")
            }
        }
    }
}
