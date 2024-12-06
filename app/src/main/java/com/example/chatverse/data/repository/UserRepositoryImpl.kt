package com.example.chatverse.data.repository

import com.example.chatverse.data.AppConstants
import com.example.chatverse.data.remote.api.AuthApi
import com.example.chatverse.data.mapper.mapFromDto
import com.example.chatverse.data.remote.dto.CheckAuthCodeDto
import com.example.chatverse.data.remote.dto.LoginResponseDto
import com.example.chatverse.data.remote.dto.PhoneBaseDto
import com.example.chatverse.domain.model.LoginResult
import com.example.chatverse.domain.repository.UserRepository
import javax.inject.Inject
import android.util.Log
import com.example.chatverse.data.TokenManager
import com.example.chatverse.data.local.dao.UserDao
import com.example.chatverse.data.local.model.UserEntity
import com.example.chatverse.data.remote.dto.RegisterInDto
import com.example.chatverse.di.AuthRetrofit
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    @AuthRetrofit private val authApi: AuthApi,
    private val tokenManager: TokenManager,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun loadUserProfile(): UserEntity? {
        return userDao.getUserById(1)
    }

    override suspend fun saveUserProfile(user: UserEntity) {
            userDao.insertUser(user)
    }

    override suspend fun saveUserProfile(registerInDto: RegisterInDto) {
        val user: UserEntity = UserEntity(
            id = 1,
            name = registerInDto.name,
            username = registerInDto.username,
            accessToken = tokenManager.getAccessToken()!!,
            refreshToken = tokenManager.getRefreshToken()!!
        )
        userDao.insertUser(user)
    }

    override suspend fun logout() {
            userDao.clearUsers()
    }

    //TODO переделать когда серверное API будет доделано
    override suspend fun registerUser(registerInDto: RegisterInDto, onResult: (Boolean, String?) -> Unit) {
            try {
                val response = authApi.registerUser(registerInDto)
                tokenManager.saveTokens(response.accessToken, response.refreshToken)

                val user: UserEntity = UserEntity(
                    id = 1,
                    name = registerInDto.name,
                    username = registerInDto.username,
                    accessToken = tokenManager.getAccessToken()!!,
                    refreshToken = tokenManager.getRefreshToken()!!
                )
                userDao.insertUser(user)

                onResult(true, null)

            } catch (e: Exception) {
                val user: UserEntity = UserEntity(
                    id = 1,
                    name = registerInDto.name,
                    username = registerInDto.username,
                    accessToken = tokenManager.getAccessToken()!!,
                    refreshToken = tokenManager.getRefreshToken()!!
                )
                userDao.insertUser(user)
                onResult(false, e.message)
            }
    }

    override suspend fun sendAuthCode(phone: String): Result<Unit> {
        return try {
            authApi.sendAuthCode(PhoneBaseDto(phone))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkAuthCode(phoneNumber: String, authCode: String): LoginResult {
        Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl checkAuthCode phoneNumber: $phoneNumber , authCode: $authCode")
        val response: LoginResponseDto = authApi.checkAuthCode(CheckAuthCodeDto(phoneNumber, authCode))
        Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl checkAuthCode response: $response")
        val loginResult: LoginResult = response.mapFromDto()

        Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl checkAuthCode loginResult: $loginResult")
        tokenManager.saveTokens(response.accessToken, response.refreshToken)
        return loginResult
    }
}