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
import com.example.chatverse.data.remote.dto.RegisterInDto
import com.example.chatverse.data.remote.dto.UserProfileDto
import com.example.chatverse.data.remote.dto.UserUpdateDto
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


    override suspend fun loadRemoteUser(): UserProfileDto {
        return mainApi.getCurrentUser()
    }

    override suspend fun updateUserProfile(user: UserUpdateDto) {
        val updateUserResponseUserUpdateDto = mainApi.updateUser(user)
        Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl - updateUserProfile updateUserResponseUserUpdateDto: $updateUserResponseUserUpdateDto")
        val oldUser = userDao.getUserById(1)
        val newUserForDB = UserProfileEntity(
            id = oldUser.id,
            name = user.name,
            username = user.username,
            birthday = user.birthday,
            city = user.city,
            vk = user.vk,
            instagram = user.instagram,
            status = user.status,
            avatar = oldUser.avatar,
            phone = oldUser.phone,
            last = oldUser.last,
            created = oldUser.created,
            online = oldUser.online,
            completedTask = oldUser.completedTask,
            accessToken = oldUser.accessToken,
            refreshToken = oldUser.refreshToken
        )
        userDao.clearUserProfile()
        userDao.insertUser(newUserForDB)


    }

    override suspend fun loadUserProfileFromDB(): UserProfileEntity {
        return userDao.getUserById(1)
    }


    override suspend fun saveUserProfileInDB(registerInDto: UserProfileDto, onResult: (Boolean, String?) -> Unit) {
        try {
            Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl - saveUserProfileInDB registerInDto: $registerInDto")
            val user = UserProfileEntity(
                id = 1,
                name = registerInDto.name!!,
                username = registerInDto.username,
                birthday = registerInDto.birthday,
                city = registerInDto.city,
                vk = registerInDto.vk,
                instagram = registerInDto.instagram,
                status = registerInDto.status,
                avatar = registerInDto.avatar,
                phone = registerInDto.phone,
                last = registerInDto.last,
                created = registerInDto.created,
                online = true,
                completedTask = 1,
                accessToken = tokenManager.getAccessToken()!!,
                refreshToken = tokenManager.getRefreshToken()!!
            )
            val resultClearDB = userDao.clearUserProfile()
            Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl - saveUserProfileInDB resultClearDB: $resultClearDB")
            val resultInsertDB = userDao.insertUser(user)
            Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl - saveUserProfileInDB resultInsertDB: $resultInsertDB")

            onResult(true, null)

        } catch (e: Exception) {
            Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl - saveUserProfileInDB Exception: $e")
        }
    }

    override suspend fun updateUserProfileInDB(
        registerInDto: UserProfileDto,
        onResult: (Boolean, String?) -> Unit
    ) {
        val user = UserProfileEntity(
            id = 1,
            name = registerInDto.name!!,
            username = registerInDto.username,
            birthday = registerInDto.birthday,
            city = registerInDto.city,
            vk = registerInDto.vk,
            instagram = registerInDto.instagram,
            status = registerInDto.status,
            avatar = registerInDto.avatar,
            phone = registerInDto.phone,
            last = registerInDto.last,
            created = registerInDto.created,
            online = true,
            completedTask = 1,
            accessToken = tokenManager.getAccessToken()!!,
            refreshToken = tokenManager.getRefreshToken()!!
        )
        val resultClearDB = userDao.clearUserProfile()
        Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl - saveUserProfileInDB resultClearDB: $resultClearDB")
        val resultInsertDB = userDao.insertUser(user)
        Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl - saveUserProfileInDB resultInsertDB: $resultInsertDB")

        loadUserProfileFromDB()

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
        Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl - registerUser - registerInDto: $registerInDto")
            try {
                val response = mainApi.registerUser(registerInDto)
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

        if (response.accessToken != null && response.refreshToken != null) {
            tokenManager.saveTokens(response.accessToken, response.refreshToken)
            Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl tokens saved/updated.")
        } else {
            Log.w(AppConstants.LOG_TAG, "UserRepositoryImpl checkAuthCode response missing tokens.")
            //TODO
            // Здесь можно обработать ситуацию, если токены почему-то null, хотя не должны быть при успехе
            // Возможно, стоит вернуть ошибку или специфический LoginResult
        }

        val loginResult: LoginResult = response.mapFromDto()
        Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl checkAuthCode loginResult: $loginResult")

        return loginResult
    }
}