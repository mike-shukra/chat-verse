package com.example.chatverse.data.remote.dto

data class LoginResponseDto(
    val refreshToken: String,
    val accessToken: String,
    val userId: Int,
    val isUserExists: Boolean
)
