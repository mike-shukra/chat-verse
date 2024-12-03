package com.example.chatverse.presentation.ui.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val authCode: String = "",
    val phoneNumber: String = "",
    val errorMessage: String? = null,
    val isUserExists: Boolean? = null,
    val loginSuccess: Boolean = false
)
