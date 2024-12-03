package com.example.chatverse.domain.model

data class LoginResult(
    val refreshToken: String,
    val accessToken: String,
    val userId: Int,
    val isUserExists: Boolean
)
