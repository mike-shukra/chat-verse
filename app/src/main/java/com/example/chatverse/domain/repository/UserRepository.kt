package com.example.chatverse.domain.repository

import com.example.chatverse.data.remote.dto.LoginOutDto
import com.example.chatverse.domain.model.User

interface UserRepository {
    suspend fun sendAuthCode(phone: String): Result<Unit>

    suspend fun checkAuthCode(phone: String, code: String): Result<LoginOutDto>
}