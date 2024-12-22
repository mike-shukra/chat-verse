package com.example.chatverse.presentation.ui.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatverse.data.AppConstants
import com.example.chatverse.data.remote.dto.UserUpdateDto
import com.example.chatverse.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
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

    fun updateProfile(name: String, userName: String, phone: String, city: String, birthDate: String, about: String) {
        this.name.value = name
        this.userName.value = userName
        this.phone.value = phone
        this.city.value = city
        this.birthDate.value = birthDate
        this.about.value = about

        viewModelScope.launch {
            val user = UserUpdateDto(
                name = name,
                username = userName,
                birthday = birthDate,
                city = city,
                vk = "",
                instagram = "",
                status = "",
                avatar = null)
            repository.updateUserProfile(user)
        }
    }

    fun loadUserProfileDB(onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
        val user = repository.loadUserProfileFromDB()
                Log.d(AppConstants.LOG_TAG, "ProfileViewModel - loadUserProfile user: $user")
                name.value = user.username
                userName.value = user.name
                avatarUrl.value = user.avatar ?: ""
                phone.value = user.phone ?: ""
                city.value = user.city ?: ""
                birthDate.value = user.birthday ?: "1970-01-01"
                zodiacSign.value = calculateZodiacSign(user.birthday ?: "1970-01-01")
                about.value = user.status ?: ""
                onResult(true, null)

        }
    }

    private fun calculateZodiacSign(birthDate: String): String {
        // Ожидается, что birthDate в формате "yyyy-MM-dd"
        val monthDay = birthDate.substring(5) // Получаем строку в формате "MM-dd"

        return when (monthDay) {
            in "01-01".."01-19" -> "Козерог"
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
            in "12-22".."12-31", in "01-01".."01-19" -> "Козерог"
            else -> "Неизвестно"
        }
    }


//    fun refreshUserProfile() {
//        viewModelScope.launch {
////            val user = repository.fetchUserProfile()
//            loadUserProfile() // Обновляем локальные данные
//        }
//    }

//    fun saveUserProfile(user: UserProfileEntity) {
//        viewModelScope.launch {
//            repository.saveUserProfile(user)
//        }
//    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
