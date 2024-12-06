package com.example.chatverse.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val username: String,
    val accessToken: String,
    val refreshToken: String
)