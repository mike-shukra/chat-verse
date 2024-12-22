package com.example.chatverse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginOutDto(
    @SerializedName("is_user_exists") val isUserExists: Boolean,
    @SerializedName("access_token") val accessToken: String?,
    @SerializedName("refresh_token") val refreshToken: String?,
    @SerializedName("user_id") val userId: Int?
)