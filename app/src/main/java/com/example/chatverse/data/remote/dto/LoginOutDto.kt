package com.example.chatverse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginOutDto(
    val isUserExists: Boolean,
    val accessToken: String?,
    val refreshToken: String?,
    val userId: Int?
)