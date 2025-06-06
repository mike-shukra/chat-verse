package com.example.chatverse.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthEvent {
    data object UserShouldLogout : AuthEvent()
}

sealed class NavigationCommand {
    data object NavigateToLoginAndClearStack : NavigationCommand()
}

@HiltViewModel
class AppWideViewModel @Inject constructor(
    private val authEventChannel: MutableSharedFlow<AuthEvent> // Предполагаем, что это тот же SharedFlow,
    // который используется в TokenAuthenticator
) : ViewModel() {

    private val _navigationCommands = MutableSharedFlow<NavigationCommand>(replay = 0, extraBufferCapacity = 1)
    val navigationCommands: SharedFlow<NavigationCommand> = _navigationCommands.asSharedFlow()

    init {
        listenToAuthEvents()
    }

    private fun listenToAuthEvents() {
        viewModelScope.launch {
            authEventChannel.collect { event ->
                when (event) {
                    is AuthEvent.UserShouldLogout -> {
                        _navigationCommands.tryEmit(NavigationCommand.NavigateToLoginAndClearStack)
                    }
                }
            }
        }
    }

    // Метод для явного выхода пользователя (например, по кнопке Logout)
    fun logoutUser() {
        viewModelScope.launch {
            // Здесь может быть логика очистки токенов через TokenManager
            // tokenManager.clearTokens() // Если TokenManager доступен здесь
            // И затем отправка команды навигации
            _navigationCommands.tryEmit(NavigationCommand.NavigateToLoginAndClearStack)
        }
    }
}