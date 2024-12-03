package com.example.chatverse.data.repository

import com.example.chatverse.data.remote.api.AuthApi
import com.example.chatverse.data.mapper.UserMapper
import com.example.chatverse.data.mapper.mapFromDto
import com.example.chatverse.data.remote.dto.CheckAuthCodeDto
import com.example.chatverse.data.remote.dto.LoginOutDto
import com.example.chatverse.data.remote.dto.LoginResponseDto
import com.example.chatverse.data.remote.dto.PhoneBaseDto
import com.example.chatverse.domain.model.LoginResult
import com.example.chatverse.domain.model.User
import com.example.chatverse.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
) : UserRepository {

    override suspend fun sendAuthCode(phone: String): Result<Unit> {
        return try {
            authApi.sendAuthCode(PhoneBaseDto(phone))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkAuthCode(phoneNumber: String, authCode: String): LoginResult {
        val response: LoginResponseDto = authApi.checkAuthCode(CheckAuthCodeDto(phoneNumber, authCode))
        return response.mapFromDto()
    }
}