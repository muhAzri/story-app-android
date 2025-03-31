package com.zrifapps.storyapp.presentation.screens.auth.register

sealed class RegisterUiState {
    data object Initial : RegisterUiState()
    data object Loading : RegisterUiState()
    data class Success(val message: String) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}
