package com.example.chatverse.domain.usecase

import com.example.chatverse.data.remote.dto.UserProfileDto
import com.example.chatverse.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserProfileToDbUseCase @Inject constructor(
    private val repository: UserRepository
) {
    // Принимает UserProfileDto (полученный от сервера) для сохранения в локальную БД
    suspend operator fun invoke(userProfile: UserProfileDto): Result<Unit> {
        return repository.saveUserProfileInDB(userProfile)
    }
}