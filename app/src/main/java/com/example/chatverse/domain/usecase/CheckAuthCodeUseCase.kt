package com.example.chatverse.domain.usecase

import android.util.Log
import com.example.chatverse.data.AppConstants
import com.example.chatverse.data.remote.dto.LoginOutDto
import com.example.chatverse.domain.model.LoginResult
import com.example.chatverse.domain.repository.UserRepository
import javax.inject.Inject

class CheckAuthCodeUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(phoneNumber: String, authCode: String): LoginResult  {
        return userRepository.checkAuthCode(phoneNumber, authCode)
    }
}
