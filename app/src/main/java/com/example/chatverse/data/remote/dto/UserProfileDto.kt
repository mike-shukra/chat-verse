package com.example.chatverse.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserProfileDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("username") val username: String,
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("vk") val vk: String?,
    @SerializedName("instagram") val instagram: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("phone") val phone: String,
    @SerializedName("last") val last: String?,
    @SerializedName("created") val created: String?,
    @SerializedName("online") val online: Boolean,
    @SerializedName("completedTask") val completedTask: Int,
    @SerializedName("avatars") val avatars: AvatarsDto
)