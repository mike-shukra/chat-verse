package com.example.chatverse.data.mapper

import com.example.chatverse.data.remote.dto.AuthResponse
import com.example.chatverse.domain.model.User
import com.example.chatverse.data.remote.dto.LoginResponseDto
import com.example.chatverse.domain.model.LoginResult

fun LoginResponseDto.mapFromDto(): LoginResult {
    return LoginResult(
        refreshToken = this.refreshToken,
        accessToken = this.accessToken,
        userId = this.userId,
        isUserExists = this.isUserExists
    )
}

object UserMapper {
    fun mapFromResponse(authResponse: AuthResponse): User {
        return User(
            id = authResponse.id,
            username = authResponse.username,
            email = authResponse.email
        )
    }
}

