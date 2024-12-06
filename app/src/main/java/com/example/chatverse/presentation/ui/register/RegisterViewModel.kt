package com.example.chatverse.presentation.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatverse.data.remote.api.AuthApi
import com.example.chatverse.data.TokenManager
import com.example.chatverse.data.local.model.UserEntity
import com.example.chatverse.data.remote.dto.RegisterInDto
import com.example.chatverse.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(

    private val  repository: UserRepository,

) : ViewModel() {

    fun registerUser(registerInDto: RegisterInDto, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            repository.registerUser(registerInDto, onResult)
        }
    }
//
//    fun saveUserProfile(registerInDto: RegisterInDto) {
//        viewModelScope.launch {
//            repository.saveUserProfile(registerInDto)
//        }
//    }
}