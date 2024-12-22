package com.example.chatverse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SuccessDto(
    @SerializedName("is_success") val isSuccess: Boolean
)