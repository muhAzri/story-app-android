package com.zrifapps.storyapp.presentation.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zrifapps.storyapp.common.network.NetworkResult
import com.zrifapps.storyapp.common.session.SessionManager
import com.zrifapps.storyapp.data.auth.request.LoginRequest
import com.zrifapps.storyapp.domain.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun register(email: String, password: String) {
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            val request = LoginRequest(email, password)
            val result = authRepository.login(request)
            _uiState.value = when (result) {
                is NetworkResult.Success -> {
                    sessionManager.saveAuthToken(result.data.loginResult.token)

                    LoginUiState.Success(
                        result.data.message
                    )
                }

                is NetworkResult.Error -> LoginUiState.Error(result.message ?: "Unknown error")
            }
        }
    }
}
