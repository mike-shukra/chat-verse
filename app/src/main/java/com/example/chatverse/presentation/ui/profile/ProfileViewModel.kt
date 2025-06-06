package com.example.chatverse.presentation.ui.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatverse.data.AppConstants
import com.example.chatverse.data.remote.dto.UserUpdateDto
import com.example.chatverse.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val  repository: UserRepository
) : ViewModel() {

    var name = mutableStateOf("")
        private set
    var userName = mutableStateOf("")
        private set
    var avatarUrl = mutableStateOf("")
        private set
    var phone = mutableStateOf("")
        private set
    var city = mutableStateOf("")
        private set
    var birthDate = mutableStateOf("")
        private set
    var zodiacSign = mutableStateOf("")
        private set
    var about = mutableStateOf("")
        private set

    var updateInProgress = mutableStateOf(false)
        private set
    var updateError = mutableStateOf<String?>(null)
        private set

    fun setProfileDetails(
        name: String,
        phone: String,
        city: String,
        birthDate: String,
        about: String
    ) {
        this.name.value = name
        this.phone.value = phone
        this.city.value = city
        this.birthDate.value = birthDate
        this.about.value = about
        // userName и avatarUrl здесь не обновляются, так как их нет в EditProfileScreen
    }

    fun updateProfile(
        newName: String,
        newPhone: String,
        newCity: String,
        newBirthDate: String,
        newAbout: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        updateInProgress.value = true
        updateError.value = null

        viewModelScope.launch {
            val userUpdateDto = UserUpdateDto(
                name = newName,
                username = userName.value, // Используем текущий userName из ViewModel
                birthday = newBirthDate,
                city = newCity,
                vk = "", // Заполните или оставьте пустыми, если не редактируются
                instagram = "",
                status = newAbout, // 'about' из формы соответствует 'status' в DTO?
                avatar = null // Аватар обрабатывается отдельно?
            )
            // Предполагаем, что repository.updateUserProfile() возвращает Result<Unit>
            val result = repository.updateUserProfile(userUpdateDto)

            result.fold(
                onSuccess = {
                    // Обновляем локальные состояния в ViewModel после успешного сохранения на сервере
                    name.value = newName
                    phone.value = newPhone // Если телефон тоже обновляется через этот DTO (сейчас нет)
                    city.value = newCity
                    birthDate.value = newBirthDate
                    about.value = newAbout // или status.value = newAbout
                    zodiacSign.value = calculateZodiacSign(newBirthDate) // Пересчитываем знак зодиака

                    updateInProgress.value = false
                    onSuccess() // Вызываем коллбэк успеха
                    Log.d(AppConstants.LOG_TAG, "Profile updated successfully.")
                },
                onFailure = { exception ->
                    updateInProgress.value = false
                    val errorMessage = exception.message ?: "Unknown error during profile update"
                    updateError.value = errorMessage
                    onError(errorMessage) // Вызываем коллбэк ошибки
                    Log.e(AppConstants.LOG_TAG, "Profile update failed: $errorMessage", exception)
                }
            )
        }
    }

    fun loadUserProfileDB(onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            repository.loadUserProfileFromDB().fold( // Используем Result из репозитория
                onSuccess = { user ->
                    Log.d(AppConstants.LOG_TAG, "ProfileViewModel - loadUserProfile user: $user")
                    name.value = user.name
                    userName.value = user.username
                    avatarUrl.value = user.avatar ?: ""
                    phone.value = user.phone ?: ""
                    city.value = user.city ?: ""
                    birthDate.value = user.birthday ?: "1970-01-01"
                    zodiacSign.value = calculateZodiacSign(user.birthday ?: "1970-01-01")
                    about.value = user.status ?: "" // Предполагаем, что 'status' в БД это 'about'
                    onResult(true, null)
                },
                onFailure = { exception ->
                    Log.e(AppConstants.LOG_TAG, "Failed to load user from DB", exception)
                    onResult(false, exception.message)
                }
            )
        }
    }

    private fun calculateZodiacSign(birthDate: String): String {
        if (birthDate.length < 10 || !birthDate.contains("-")) return "Неизвестно"
        val monthDay = birthDate.substring(5, 10) // "MM-dd"
        return when (monthDay) {
            in "01-20".."02-18" -> "Водолей"
            in "02-19".."03-20" -> "Рыбы"
            in "03-21".."04-19" -> "Овен"
            in "04-20".."05-20" -> "Телец"
            in "05-21".."06-20" -> "Близнецы"
            in "06-21".."07-22" -> "Рак"
            in "07-23".."08-22" -> "Лев"
            in "08-23".."09-22" -> "Дева"
            in "09-23".."10-22" -> "Весы"
            in "10-23".."11-21" -> "Скорпион"
            in "11-22".."12-21" -> "Стрелец"
            in "12-22".."12-31" -> "Козерог"
            in "01-01".."01-19" -> "Козерог"
            else -> "Неизвестно"
        }
    }


    fun logout() {
        viewModelScope.launch {
            repository.logout()
            // Здесь можно добавить сброс состояний ViewModel к значениям по умолчанию
            name.value = ""
            // ... и так далее для всех полей
        }
    }

    fun clearUpdateError() {
        updateError.value = null
    }
}
