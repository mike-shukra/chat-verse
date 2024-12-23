package com.example.chatverse.domain.usecase

import com.example.chatverse.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendAuthCodeUseCase @Inject constructor(
    private val  repository: UserRepository
) {
    suspend operator fun invoke(phone: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            repository.sendAuthCode(phone)
        }
    }
}
