package com.example.chatverse.domain.repository

import com.example.chatverse.data.local.model.UserProfileEntity
import com.example.chatverse.data.remote.dto.RegisterInDto
import com.example.chatverse.data.remote.dto.UserProfileDto
import com.example.chatverse.data.remote.dto.UserUpdateDto
import com.example.chatverse.domain.model.LoginResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun sendAuthCode(phone: String): Result<Unit>

    suspend fun checkAuthCode(phoneNumber: String, authCode: String): LoginResult
    suspend fun registerUser(
        registerInDto: RegisterInDto,
        onResult: (Boolean, String?) -> Unit
    )
    suspend fun loadUserProfileFromDB(): UserProfileEntity
    suspend fun saveUserProfileInDB(registerInDto: UserProfileDto, onResult: (Boolean, String?) -> Unit)
    suspend fun logout()
    suspend fun loadRemoteUser(): UserProfileDto
    suspend fun updateUserProfile(user: UserUpdateDto)
    suspend fun updateUserProfileInDB(
        registerInDto: UserProfileDto,
        onResult: (Boolean, String?) -> Unit
    )

    suspend fun saveUserProfile(registerInDto: RegisterInDto)
}