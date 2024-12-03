package com.example.chatverse.data.remote.dto

data class UserProfileSendDto(
    val id: Int,
    val name: String,
    val username: String,
    val birthday: String?,
    val city: String?,
    val vk: String?,
    val instagram: String?,
    val status: String?,
    val avatar: String?,
    val phone: String,
    val last: String?,
    val created: String?,
    val online: Boolean,
    val completedTask: Int,
    val avatars: AvatarsDto
)