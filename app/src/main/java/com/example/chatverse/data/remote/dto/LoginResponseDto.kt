package com.example.chatverse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("refreshToken") val refreshToken: String?,
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("userId") val userId: Int,
    @SerializedName("userExists") val userExists: Boolean
)