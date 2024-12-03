package com.example.chatverse.data.repository

import com.example.chatverse.data.remote.api.AuthApi
import com.example.chatverse.data.mapper.UserMapper
import com.example.chatverse.data.remote.dto.CheckAuthCodeDto
import com.example.chatverse.data.remote.dto.LoginOutDto
import com.example.chatverse.data.remote.dto.PhoneBaseDto
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

    override suspend fun checkAuthCode(phone: String, code: String): Result<LoginOutDto> {
        return try {
            val response = authApi.checkAuthCode(CheckAuthCodeDto(phone, code))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}