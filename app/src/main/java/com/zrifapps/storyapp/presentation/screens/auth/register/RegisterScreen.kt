package com.zrifapps.storyapp.presentation.screens.auth.register

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
import androidx.compose.material.icons.filled.Person
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
import com.zrifapps.storyapp.common.util.ValidationUtil
import com.zrifapps.storyapp.presentation.components.button.CustomButton
import com.zrifapps.storyapp.presentation.components.textfield.CustomTextField
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var showContent by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val nameErrorEmpty = stringResource(R.string.name_error_empty)
    val emailErrorEmpty = stringResource(R.string.email_error_empty)
    val emailErrorInvalid = stringResource(R.string.email_error_invalid)
    val passwordErrorEmpty = stringResource(R.string.password_error_empty)
    val passwordErrorInvalid = stringResource(R.string.password_error_invalid)
    val confirmPasswordErrorEmpty = stringResource(R.string.confirm_password_error_empty)
    val confirmPasswordErrorMismatch = stringResource(R.string.confirm_password_error_mismatch)

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        showContent = true
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is RegisterUiState.Success -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.register_succeed),
                    duration = SnackbarDuration.Short
                )
                delay(2000)
                onNavigateToLogin()
            }

            is RegisterUiState.Error -> {
                snackbarHostState.showSnackbar((uiState as RegisterUiState.Error).message)
            }

            else -> {}
        }
    }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    fun validateFields(): Boolean {
        nameError = if (name.isEmpty()) nameErrorEmpty else null
        emailError = when {
            email.isEmpty() -> emailErrorEmpty
            !ValidationUtil.validateEmail(email) -> emailErrorInvalid
            else -> null
        }
        passwordError = when {
            password.isEmpty() -> passwordErrorEmpty
            !ValidationUtil.validatePassword(password) -> passwordErrorInvalid
            else -> null
        }
        confirmPasswordError = when {
            confirmPassword.isEmpty() -> confirmPasswordErrorEmpty
            confirmPassword != password -> confirmPasswordErrorMismatch
            else -> null
        }

        return nameError == null && emailError == null &&
                passwordError == null && confirmPasswordError == null
    }

    fun attemptRegister() {
        if (validateFields()) {
            viewModel.register(name, email, password)
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
                        stringResource(R.string.create_account),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        stringResource(R.string.sign_up_message),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(36.dp))

                    CustomTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = if (it.isEmpty()) nameErrorEmpty else null
                        },
                        label = stringResource(R.string.name_label),
                        leadingIcon = Icons.Default.Person,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }),
                        errorText = nameError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = when {
                                it.isEmpty() -> emailErrorEmpty
                                !ValidationUtil.validateEmail(it) -> emailErrorInvalid
                                else -> null
                            }
                        },
                        label = stringResource(R.string.email_label),
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
                                !ValidationUtil.validatePassword(it) -> passwordErrorInvalid
                                else -> null
                            }
                        },
                        label = stringResource(R.string.password_label),
                        leadingIcon = Icons.Default.Lock,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }),
                        errorText = passwordError,
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            confirmPasswordError = when {
                                it.isEmpty() -> confirmPasswordErrorEmpty
                                it != password -> confirmPasswordErrorMismatch
                                else -> null
                            }
                        },
                        label = stringResource(R.string.confirm_password_label),
                        leadingIcon = Icons.Default.Lock,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            attemptRegister()
                        }),
                        errorText = confirmPasswordError,
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    CustomButton(
                        text = stringResource(R.string.register),
                        isLoading = uiState is RegisterUiState.Loading,
                        onClick = {
                            focusManager.clearFocus()
                            attemptRegister()
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(onClick = onNavigateToLogin) {
                        Text(
                            stringResource(R.string.already_have_account),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
