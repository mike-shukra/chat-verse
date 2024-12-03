package com.example.chatverse.data.remote.dto

data class LoginOutDto(
    val isUserExists: Boolean,
    val accessToken: String?,
    val refreshToken: String?,
    val userId: Int?
)