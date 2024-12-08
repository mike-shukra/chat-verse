package com.example.chatverse.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.chatverse.data.remote.dto.AvatarsDto
import com.google.gson.annotations.SerializedName

@Entity(tableName = "userprofile")
data class UserProfileEntity(
    @PrimaryKey val id: Int,
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
    @SerializedName("completed_task") val completedTask: Int,
    val accessToken: String,
    val refreshToken: String
)