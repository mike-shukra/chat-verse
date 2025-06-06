package com.example.chatverse.domain.repository

import com.example.chatverse.data.local.model.UserProfileEntity
import com.example.chatverse.data.remote.dto.RegisterInDto
import com.example.chatverse.data.remote.dto.UserProfileDto
import com.example.chatverse.data.remote.dto.UserUpdateDto
import com.example.chatverse.domain.model.LoginResult

interface UserRepository {
    suspend fun sendAuthCode(phone: String): Result<Unit>
    suspend fun checkAuthCode(phoneNumber: String, authCode: String): Result<LoginResult> // Возвращаем Result

    suspend fun loadRemoteUser(): Result<UserProfileDto> // Возвращаем Result
    suspend fun updateUserProfile(user: UserUpdateDto): Result<Unit> // Возвращаем Result

    suspend fun loadUserProfileFromDB(): Result<UserProfileEntity> // Возвращаем Result

    // Изменено: теперь suspend и возвращает Result<Unit> (или Result<String> если бы был реальный ID)
    suspend fun saveUserProfileInDB(userProfileDto: UserProfileDto): Result<Unit>

    suspend fun updateUserProfileInDB(userProfileDto: UserProfileDto): Result<Unit> // Аналогично

    // Пока оставляем как есть, но тоже можно переделать на Result
    suspend fun registerUser(registerInDto: RegisterInDto, onResult: (Boolean, String?) -> Unit)
    suspend fun saveUserProfile(registerInDto: RegisterInDto) // TODO
    suspend fun logout(): Result<Unit> // Возвращаем Result
}