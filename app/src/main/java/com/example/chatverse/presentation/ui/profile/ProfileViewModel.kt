package com.example.chatverse.presentation.ui.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatverse.data.AppConstants
import com.example.chatverse.data.local.dao.UserDao
import com.example.chatverse.data.local.model.UserEntity
import com.example.chatverse.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val  repository: UserRepository
) : ViewModel() {

    var userName = mutableStateOf("")
        private set
    var userFullName = mutableStateOf("")
        private set
    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val user = repository.loadUserProfile()
            Log.d(AppConstants.LOG_TAG, "ProfileViewModel - loadUserProfile user: $user")
            if (user != null) {
                userName.value = user.username
                userFullName.value = user.name
            }
        }
    }

    fun saveUserProfile(user: UserEntity) {
        viewModelScope.launch {
            repository.saveUserProfile(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
