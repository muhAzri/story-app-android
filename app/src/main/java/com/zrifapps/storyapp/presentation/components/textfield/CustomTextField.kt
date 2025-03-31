package com.zrifapps.storyapp.presentation.components.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    errorText: String?,
    isPassword: Boolean = false,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(leadingIcon, contentDescription = "$label Icon") },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (isPasswordVisible) "Hide Password" else "Show Password"
                    )
                }
            }
        },
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        isError = errorText != null,
        supportingText = {
            AnimatedVisibility(
                visible = errorText != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                errorText?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}
