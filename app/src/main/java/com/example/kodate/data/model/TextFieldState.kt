package com.example.kodate.data.model

data class TextFieldState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val name: String = "",
    val age: Int = 18,
    val bio: String = "",
    val city: String = "",
    val isMale: Boolean = true,
    val interests: List<String> = emptyList(),
    val chatMessage: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val confirmPasswordError: String = "",
    val ageError: String = ""
)
