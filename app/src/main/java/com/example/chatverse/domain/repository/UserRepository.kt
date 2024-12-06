package com.example.chatverse.domain.repository

import com.example.chatverse.data.local.model.UserEntity
import com.example.chatverse.data.remote.dto.RegisterInDto
import com.example.chatverse.domain.model.LoginResult

interface UserRepository {
    suspend fun sendAuthCode(phone: String): Result<Unit>

    suspend fun checkAuthCode(phoneNumber: String, authCode: String): LoginResult
    suspend fun registerUser(
        registerInDto: RegisterInDto,
        onResult: (Boolean, String?) -> Unit
    )

    suspend fun loadUserProfile(): UserEntity?
    suspend fun saveUserProfile(user: UserEntity)
    suspend fun logout()
    suspend fun saveUserProfile(registerInDto: RegisterInDto)
}