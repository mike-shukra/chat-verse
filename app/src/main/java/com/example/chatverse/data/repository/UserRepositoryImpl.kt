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
import com.example.chatverse.data.local.model.UserProfileEntity
import com.example.chatverse.data.remote.api.MainApi
import com.example.chatverse.data.remote.dto.GetCurrentUserProfileDto
import com.example.chatverse.data.remote.dto.RegisterInDto
import com.example.chatverse.di.AuthRetrofit
import com.example.chatverse.di.MainRetrofit
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    @AuthRetrofit private val authApi: AuthApi,
    @MainRetrofit private val mainApi: MainApi,
    private val tokenManager: TokenManager,
    private val userDao: UserDao
) : UserRepository {


    override suspend fun loadRemoteUser(): GetCurrentUserProfileDto {
        return mainApi.getCurrentUser()
    }
    override suspend fun loadUserProfile(): UserProfileEntity? {
        return userDao.getUserById(1)
    }

    override suspend fun loadProfile(): UserProfileEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun saveUserProfile(registerInDto: GetCurrentUserProfileDto, onResult: (Boolean, String?) -> Unit) {
        val user = UserProfileEntity(
            id = 1,
            name = registerInDto.profileData.name,
            username = registerInDto.profileData.username,
            birthday = registerInDto.profileData.birthday,
            city = registerInDto.profileData.city,
            vk = registerInDto.profileData.vk,
            instagram = registerInDto.profileData.instagram,
            status = registerInDto.profileData.status,
            avatar = registerInDto.profileData.avatar,
            phone = registerInDto.profileData.phone,
            last = registerInDto.profileData.last,
            created = registerInDto.profileData.created,
            online = true,
            completedTask = 1,
            accessToken = tokenManager.getAccessToken()!!,
            refreshToken = tokenManager.getRefreshToken()!!
        )
        userDao.insertUser(user)
        onResult(true, null)
    }

    override suspend fun saveUserProfile(registerInDto: RegisterInDto) {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
            userDao.clearUsers()
    }

    //TODO переделать когда серверное API будет доделано
    override suspend fun registerUser(registerInDto: RegisterInDto, onResult: (Boolean, String?) -> Unit) {
            try {
                val response = authApi.registerUser(registerInDto)
                tokenManager.saveTokens(response.accessToken, response.refreshToken)

                val user = UserProfileEntity(
                    id = 1,
                    name = registerInDto.name,
                    username = registerInDto.username,
                    birthday = "1970-01-01",
                    city = "",
                    vk = "",
                    instagram = "",
                    status = "",
                    avatar = "",
                    phone = "",
                    last = "",
                    created = "",
                    online = true,
                    completedTask = 1,
                    accessToken = tokenManager.getAccessToken()!!,
                    refreshToken = tokenManager.getRefreshToken()!!
                )
                userDao.insertUser(user)
                onResult(true, null)

            } catch (e: Exception) {
                val user = UserProfileEntity(
                    id = 1,
                    name = registerInDto.name,
                    username = registerInDto.username,
                    birthday = "1970-01-01",
                    city = "",
                    vk = "",
                    instagram = "",
                    status = "",
                    avatar = "",
                    phone = "",
                    last = "",
                    created = "",
                    online = true,
                    completedTask = 1,
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