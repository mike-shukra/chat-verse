package com.example.chatverse.domain.usecase

import com.example.chatverse.domain.repository.UserRepository

class LoginUseCase(private val userRepository: UserRepository) {

//    suspend operator fun invoke(username: String, password: String): Result<Unit> {
//        return try {
//            userRepository.login(username, password)
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
}
