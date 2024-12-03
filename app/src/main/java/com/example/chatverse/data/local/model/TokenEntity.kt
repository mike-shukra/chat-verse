package com.example.chatverse.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tokens")
data class TokenEntity(
    @PrimaryKey val userId: Int,
    val accessToken: String,
    val refreshToken: String
)