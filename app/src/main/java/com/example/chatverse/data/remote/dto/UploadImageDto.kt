package com.example.chatverse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UploadImageDto(
    val filename: String,
    @SerializedName("base_64") val base64: String
)