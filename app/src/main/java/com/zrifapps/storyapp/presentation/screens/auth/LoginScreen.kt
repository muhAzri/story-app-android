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
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }

    val emailErrorEmpty = stringResource(id = R.string.email_error_empty)
    val emailErrorInvalid = stringResource(id = R.string.email_error_invalid)
    val passwordErrorEmpty = stringResource(id = R.string.password_error_empty)
    val passwordErrorInvalid = stringResource(id = R.string.password_error_invalid)

    LaunchedEffect(Unit) {
        showContent = true
    }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    fun attemptLogin() {
        if (emailError == null && passwordError == null && email.isNotEmpty() && password.isNotEmpty()) {
            isLoading = true
            onLoginSuccess()
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
                            !ValidationUtil.validateEmail(it) -> emailErrorInvalid
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
                            !ValidationUtil.validatePassword(it) -> passwordErrorInvalid
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
                    isLoading = isLoading,
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
