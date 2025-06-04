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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    @AuthRetrofit private val authApi: AuthApi,
    @MainRetrofit private val mainApi: MainApi,
    private val tokenManager: TokenManager,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun sendAuthCode(phone: String): Result<Unit> {
        return try {
            authApi.sendAuthCode(PhoneBaseDto(phone))
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(AppConstants.LOG_TAG, "sendAuthCode failed", e)
            Result.failure(e)
        }
    }

    override suspend fun checkAuthCode(phoneNumber: String, authCode: String): Result<LoginResult> {
        return try {
            Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl checkAuthCode phoneNumber: $phoneNumber , authCode: $authCode")
            val response: LoginResponseDto = authApi.checkAuthCode(CheckAuthCodeDto(phoneNumber, authCode))
            Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl checkAuthCode response: $response")

            if (response.accessToken != null && response.refreshToken != null) {
                tokenManager.saveTokens(response.accessToken, response.refreshToken)
                Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl tokens saved/updated.")
            } else {
                Log.w(AppConstants.LOG_TAG, "UserRepositoryImpl checkAuthCode response missing tokens.")
                // Можно вернуть специфическую ошибку, если это критично
                return Result.failure(Exception("Access or Refresh token is missing in response."))
            }

            val loginResult: LoginResult = response.mapFromDto()
            Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl checkAuthCode loginResult: $loginResult")
            Result.success(loginResult)
        } catch (e: Exception) {
            Log.e(AppConstants.LOG_TAG, "checkAuthCode failed", e)
            Result.failure(e)
        }
    }

    override suspend fun loadRemoteUser(): Result<UserProfileDto> {
        return try {
            Result.success(mainApi.getCurrentUser())
        } catch (e: Exception) {
            Log.e(AppConstants.LOG_TAG, "loadRemoteUser failed", e)
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(user: UserUpdateDto): Result<Unit> {
        return try {
            val updateUserResponseUserUpdateDto = mainApi.updateUser(user)
            Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl - updateUserProfile response: $updateUserResponseUserUpdateDto")
            // Здесь можно добавить обновление в локальной БД, если нужно, используя updateUserProfileInDB
            // Например:
            // val oldLocalUser = userDao.getUserById(1) // Предполагаем, что ID известен или есть другой способ найти юзера
            // val updatedProfileDto = UserProfileDto(name = user.name, username = user.username, /*... остальные поля из oldLocalUser или user ...*/)
            // updateUserProfileInDB(updatedProfileDto) // Это вызовет обновление в локальной БД
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(AppConstants.LOG_TAG, "updateUserProfile failed", e)
            Result.failure(e)
        }
    }

    override suspend fun loadUserProfileFromDB(): Result<UserProfileEntity> {
        return withContext(Dispatchers.IO) { // Работа с БД в IO потоке
            try {
                Result.success(userDao.getUserById(1)) // Предполагаем ID = 1
            } catch (e: Exception) {
                Log.e(AppConstants.LOG_TAG, "loadUserProfileFromDB failed", e)
                Result.failure(e)
            }
        }
    }

    override suspend fun saveUserProfileInDB(userProfileDto: UserProfileDto): Result<Unit> {
        return withContext(Dispatchers.IO) { // Работа с БД в IO потоке
            try {
                Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl - saveUserProfileInDB userProfileDto: $userProfileDto")
                val user = UserProfileEntity(
                    id = 1, // Хардкод ID
                    name = userProfileDto.name ?: "", // Безопасное извлечение
                    username = userProfileDto.username,
                    birthday = userProfileDto.birthday,
                    city = userProfileDto.city,
                    vk = userProfileDto.vk,
                    instagram = userProfileDto.instagram,
                    status = userProfileDto.status,
                    avatar = userProfileDto.avatar,
                    phone = userProfileDto.phone ?: "", // Безопасное извлечение
                    last = userProfileDto.last,
                    created = userProfileDto.created,
                    online = true, // Предполагаем, что при сохранении пользователь онлайн
                    completedTask = 1, // Значение по умолчанию или из DTO
                    accessToken = tokenManager.getAccessToken() ?: "", // Безопасное извлечение
                    refreshToken = tokenManager.getRefreshToken() ?: "" // Безопасное извлечение
                )
                userDao.clearUserProfile() // Очистка старых данных (если всегда один пользователь)
                userDao.insertUser(user)
                Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl - saveUserProfileInDB success")
                Result.success(Unit)
            } catch (e: Exception) {
                Log.e(AppConstants.LOG_TAG, "saveUserProfileInDB failed", e)
                Result.failure(e)
            }
        }
    }


    override suspend fun updateUserProfileInDB(userProfileDto: UserProfileDto): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl - updateUserProfileInDB userProfileDto: $userProfileDto")
                // Предполагаем, что ID=1 существует и мы его обновляем
                // Можно сначала загрузить существующего пользователя, чтобы не потерять поля, которых нет в DTO
                val existingUser = userDao.getUserById(1) // Может выбросить исключение, если нет юзера

                val updatedUser = UserProfileEntity(
                    id = existingUser.id,
                    name = userProfileDto.name ?: existingUser.name,
                    username = userProfileDto.username ?: existingUser.username, // Обновляем или оставляем старое
                    birthday = userProfileDto.birthday ?: existingUser.birthday,
                    city = userProfileDto.city ?: existingUser.city,
                    vk = userProfileDto.vk ?: existingUser.vk,
                    instagram = userProfileDto.instagram ?: existingUser.instagram,
                    status = userProfileDto.status ?: existingUser.status,
                    avatar = userProfileDto.avatar ?: existingUser.avatar, // Если аватар можно обновлять через это DTO
                    phone = userProfileDto.phone ?: existingUser.phone,
                    last = userProfileDto.last ?: existingUser.last, // или всегда обновлять `last` на текущее время
                    created = existingUser.created, // `created` обычно не меняется
                    online = userProfileDto.online ?: existingUser.online, // или всегда true при обновлении
                    completedTask = userProfileDto.completedTask ?: existingUser.completedTask, // если есть в DTO
                    accessToken = tokenManager.getAccessToken() ?: existingUser.accessToken,
                    refreshToken = tokenManager.getRefreshToken() ?: existingUser.refreshToken
                )
                // userDao.clearUserProfile() // Не нужно, если мы обновляем по ID
                userDao.insertUser(updatedUser) // insertUser с тем же ID обновит запись (если onConflict = REPLACE)
                Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl - updateUserProfileInDB success")
                Result.success(Unit)
            } catch (e: Exception) {
                Log.e(AppConstants.LOG_TAG, "updateUserProfileInDB failed", e)
                Result.failure(e)
            }
        }
    }


    override suspend fun saveUserProfile(registerInDto: RegisterInDto) {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                userDao.clearUsers() // Или более специфичная очистка данных пользователя
                tokenManager.clearTokens() // Очистка токенов
                Log.d(AppConstants.LOG_TAG, "User data cleared on logout.")
                Result.success(Unit)
            } catch (e: Exception) {
                Log.e(AppConstants.LOG_TAG, "logout failed", e)
                Result.failure(e)
            }
        }
    }

    //TODO переделать когда серверное API будет доделано
    override suspend fun registerUser(registerInDto: RegisterInDto, onResult: (Boolean, String?) -> Unit) {
        Log.d(AppConstants.LOG_TAG, "UserRepositoryImpl - registerUser - registerInDto: $registerInDto")
//        try {
//            val response = mainApi.registerUser(registerInDto) // Предполагается, что это suspend функция
//            tokenManager.saveTokens(response.accessToken, response.refreshToken)
//
//            // После регистрации можно сразу сохранить профиль в БД
//            // Это UserProfileDto, а не RegisterInDto, нужно будет смапить или создать
//            val userProfileToSave = UserProfileDto(
//                name = registerInDto.name,
//                username = registerInDto.username,
//                phone = registerInDto.phone, // Если телефон есть в RegisterInDto
//                // ... другие поля по умолчанию или из RegisterInDto
//            )
//            saveUserProfileInDB(userProfileToSave).fold(
//                onSuccess = { onResult(true, null) },
//                onFailure = { onResult(false, it.message) }
//            )
//        } catch (e: Exception) {
//            Log.e(AppConstants.LOG_TAG, "registerUser failed", e)
//            // Возможно, стоит удалить временного пользователя, если он был создан до ошибки
//            onResult(false, e.message)
//        }
    }
}