package com.zrifapps.storyapp.presentation.screens.auth

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zrifapps.storyapp.R
import com.zrifapps.storyapp.common.util.ValidationUtil
import com.zrifapps.storyapp.presentation.components.button.CustomButton
import com.zrifapps.storyapp.presentation.components.textfield.CustomTextField

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }

    val nameErrorEmpty = stringResource(R.string.name_error_empty)
    val emailErrorEmpty = stringResource(R.string.email_error_empty)
    val emailErrorInvalid = stringResource(R.string.email_error_invalid)
    val passwordErrorEmpty = stringResource(R.string.password_error_empty)
    val passwordErrorInvalid = stringResource(R.string.password_error_invalid)
    val confirmPasswordErrorEmpty = stringResource(R.string.confirm_password_error_empty)
    val confirmPasswordErrorMismatch = stringResource(R.string.confirm_password_error_mismatch)


    LaunchedEffect(Unit) {
        showContent = true
    }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    fun attemptRegister() {
        if (nameError == null && emailError == null && passwordError == null && confirmPasswordError == null &&
            name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword == password
        ) {
            isLoading = true
            onRegisterSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(animationSpec = tween(1000)) + expandVertically(
                animationSpec = tween(
                    1000
                )
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
                    isLoading = isLoading,
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
