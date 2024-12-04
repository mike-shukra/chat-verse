package com.example.chatverse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RefreshTokenDto(
    @SerializedName("refresh_token") val refreshToken: String
)