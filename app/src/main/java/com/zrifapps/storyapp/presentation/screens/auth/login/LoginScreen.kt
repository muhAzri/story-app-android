package com.zrifapps.storyapp.presentation.screens.auth.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zrifapps.storyapp.R
import com.zrifapps.storyapp.common.util.ValidationUtils
import com.zrifapps.storyapp.presentation.components.button.CustomButton
import com.zrifapps.storyapp.presentation.components.textfield.CustomTextField
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var showContent by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val emailErrorEmpty = stringResource(id = R.string.email_error_empty)
    val emailErrorInvalid = stringResource(id = R.string.email_error_invalid)
    val passwordErrorEmpty = stringResource(id = R.string.password_error_empty)
    val passwordErrorInvalid = stringResource(id = R.string.password_error_invalid)

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        showContent = true
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.Success -> {

                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.login_succeed),
                    duration = SnackbarDuration.Short
                )
                delay(2000)
                onLoginSuccess()
            }

            is LoginUiState.Error -> {
                snackbarHostState.showSnackbar((uiState as LoginUiState.Error).message)
            }

            else -> {}
        }
    }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    fun validateFields(): Boolean {
        emailError = when {
            email.isEmpty() -> emailErrorEmpty
            !ValidationUtils.validateEmail(email) -> emailErrorInvalid
            else -> null
        }
        passwordError = when {
            password.isEmpty() -> passwordErrorEmpty
            !ValidationUtils.validatePassword(password) -> passwordErrorInvalid
            else -> null
        }

        return emailError == null && passwordError == null
    }

    fun attemptLogin() {
        if (validateFields()) {
            viewModel.register(
                email,
                password
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(1000)) + expandVertically(
                    animationSpec = tween(1000)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(id = R.string.welcome_back),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        stringResource(id = R.string.sign_in_message),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(36.dp))

                    CustomTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = when {
                                it.isEmpty() -> emailErrorEmpty
                                !ValidationUtils.validateEmail(it) -> emailErrorInvalid
                                else -> null
                            }
                        },
                        label = stringResource(id = R.string.email_label),
                        leadingIcon = Icons.Default.Email,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }),
                        errorText = emailError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = when {
                                it.isEmpty() -> passwordErrorEmpty
                                !ValidationUtils.validatePassword(it) -> passwordErrorInvalid
                                else -> null
                            }
                        },
                        label = stringResource(id = R.string.password_label),
                        leadingIcon = Icons.Default.Lock,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            attemptLogin()
                        }),
                        errorText = passwordError,
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    CustomButton(
                        text = stringResource(id = R.string.login),
                        isLoading = uiState is LoginUiState.Loading,
                        onClick = {
                            focusManager.clearFocus()
                            attemptLogin()
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(onClick = onNavigateToRegister) {
                        Text(
                            stringResource(id = R.string.register_prompt),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
