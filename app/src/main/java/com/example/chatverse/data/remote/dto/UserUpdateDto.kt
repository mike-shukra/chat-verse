package com.example.chatverse.data.remote.dto

data class UserUpdateDto(
    val name: String,
    val username: String,
    val birthday: String?,
    val city: String?,
    val vk: String?,
    val instagram: String?,
    val status: String?,
    val avatar: UploadImageDto?
)