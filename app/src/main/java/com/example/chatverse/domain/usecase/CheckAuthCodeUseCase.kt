//package com.example.chatverse.domain.usecase
//
//import com.example.chatverse.domain.model.LoginResult
//import com.example.chatverse.domain.repository.UserRepository
//
//class CheckAuthCodeUseCase(
//    private val userRepository: UserRepository
//) {
//    suspend operator fun invoke(phoneNumber: String, authCode: String): LoginResult {
//        return userRepository.checkAuthCode(phoneNumber, authCode)
//    }
//}
