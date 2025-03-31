package com.zrifapps.storyapp.common.util

object ValidationUtil {
    fun validateEmail(input: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return input.matches(emailRegex.toRegex())
    }

    fun validatePassword(input: String): Boolean {
        return input.length >= 8
    }
}
