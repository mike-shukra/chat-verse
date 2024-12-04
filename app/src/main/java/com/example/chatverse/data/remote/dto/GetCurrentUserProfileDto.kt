package com.example.chatverse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GetCurrentUserProfileDto(
    @SerializedName("profile_data") val profileData: UserProfileSendDto
)
