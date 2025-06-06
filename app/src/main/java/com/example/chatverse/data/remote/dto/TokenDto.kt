package com.example.chatverse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TokenDto(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int
)