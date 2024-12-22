package com.example.chatverse.data.mapper

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

//object UserMapper {
//    fun mapFromResponse(authResponse: AuthResponse): UserEntity {
//        return UserEntity(
//            id = authResponse.id,
//            username = authResponse.username,
//        )
//    }
//}

