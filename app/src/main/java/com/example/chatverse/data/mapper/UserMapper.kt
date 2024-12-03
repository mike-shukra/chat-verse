package com.example.chatverse.data.mapper

import com.example.chatverse.data.remote.dto.AuthResponse
import com.example.chatverse.domain.model.User

object UserMapper {
    fun mapFromResponse(authResponse: AuthResponse): User {
        return User(
            id = authResponse.id,
            username = authResponse.username,
            email = authResponse.email
        )
    }
}