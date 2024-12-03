package com.example.chatverse.data.remote.dto

data class TokenDto(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int
)