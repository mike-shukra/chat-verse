package com.example.chatverse.domain.repository

import com.example.chatverse.data.remote.dto.LoginOutDto
import com.example.chatverse.domain.model.LoginResult
import com.example.chatverse.domain.model.User

interface UserRepository {
    suspend fun sendAuthCode(phone: String): Result<Unit>

    suspend fun checkAuthCode(phoneNumber: String, authCode: String): LoginResult
}