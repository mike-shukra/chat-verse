package com.example.chatverse.domain.usecase

import com.example.chatverse.data.remote.dto.UserProfileDto
import com.example.chatverse.domain.repository.UserRepository
import javax.inject.Inject

class LoadRemoteUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<UserProfileDto> {
        return repository.loadRemoteUser()
    }
}