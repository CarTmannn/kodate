package com.example.kodate.data.model

data class SignUpState (
    val isLoading: Boolean = false,
    val errorMessage: String? = "",
    val isSuccess: Boolean = false,
    val isEmailSuccess: Boolean = false,
    val isUploadImageSuccess: Boolean = false,
    val isEmailExists: Boolean = false
)
