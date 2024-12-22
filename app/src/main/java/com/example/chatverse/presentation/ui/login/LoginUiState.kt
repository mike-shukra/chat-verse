package com.example.chatverse.presentation.ui.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false,
    val isUserExists: Boolean? = null,
    val phoneNumber: String = "",
    val authCode: String = "",
    val countryCode: String = "+1",
    val countries: List<Pair<String, String>> = listOf(
        "US" to "+1",
        "RU" to "+7",
        "IN" to "+91",
        "FR" to "+33",
        "DE" to "+49"
    )
)
