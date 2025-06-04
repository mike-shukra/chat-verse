package com.example.chatverse.presentation.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatverse.data.AppConstants
import com.example.chatverse.domain.usecase.CheckAuthCodeUseCase
import com.example.chatverse.domain.usecase.LoadRemoteUserUseCase
import com.example.chatverse.domain.usecase.SaveUserProfileToDbUseCase
import com.example.chatverse.domain.usecase.SendAuthCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sendAuthCodeUseCase: SendAuthCodeUseCase,
    private val checkAuthCodeUseCase: CheckAuthCodeUseCase,
    private val loadRemoteUserUseCase: LoadRemoteUserUseCase,
    private val saveUserProfileToDbUseCase: SaveUserProfileToDbUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phoneNumber)
    }

    fun onAuthCodeChange(authCode: String) {
        _uiState.value = _uiState.value.copy(authCode = authCode)
    }

    fun onCountrySelected(countryCode: String) {
        _uiState.value = _uiState.value.copy(countryCode = countryCode)
    }

    fun sendAuthCode() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val fullPhoneNumber = uiState.value.countryCode + uiState.value.phoneNumber
            sendAuthCodeUseCase(fullPhoneNumber)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, authCodeSent = true) } // Добавим флаг, если нужно
                    Log.d(AppConstants.LOG_TAG, "Auth code sent successfully.")
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = exception.message) }
                    Log.e(AppConstants.LOG_TAG, "Send auth code failed: ${exception.message}", exception)
                }
        }
    }

    fun checkAuthCode() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val fullPhoneNumber = uiState.value.countryCode + uiState.value.phoneNumber
            val authCode = uiState.value.authCode

            checkAuthCodeUseCase(fullPhoneNumber, authCode)
                .onSuccess { loginResult ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isUserExists = loginResult.isUserExists, // Предполагаем, что LoginResult имеет это поле
                            loginSuccess = true // Сигнализируем об успехе проверки кода
                        )
                    }
                    Log.d(AppConstants.LOG_TAG, "Check auth code success. User exists: ${loginResult.isUserExists}")
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = exception.message) }
                    Log.e(AppConstants.LOG_TAG, "Check auth code failed: ${exception.message}", exception)
                }
        }
    }

    // Этот метод вызывается из onLoginSuccess в Composable после того, как loginSuccess стал true
    fun completeLoginAndSaveUser(onFinished: (isNewUser: Boolean) -> Unit) {
        viewModelScope.launch {
            val isNewUser = !(uiState.value.isUserExists ?: false) // Если isUserExists null, считаем новым (или другая логика)

            loadRemoteUserUseCase()
                .onSuccess { userProfileDto ->
                    Log.d(AppConstants.LOG_TAG, "Remote user loaded: $userProfileDto")
                    saveUserProfileToDbUseCase(userProfileDto)
                        .onSuccess {
                            Log.d(AppConstants.LOG_TAG, "User profile saved to DB successfully.")
                            _uiState.update { it.copy(finalLoading = false) } // Если есть какой-то финальный индикатор
                            onFinished(isNewUser)
                        }
                        .onFailure { dbException ->
                            Log.e(AppConstants.LOG_TAG, "Failed to save user profile to DB: ${dbException.message}", dbException)
                            _uiState.update { it.copy(finalLoading = false, errorMessage = "Failed to save profile: ${dbException.message}") }
                            // Решить, что делать дальше. Возможно, все равно вызвать onFinished, но с ошибкой?
                            // Или считать, что логин не завершен полностью.
                            // Для примера, все равно вызываем onFinished, но UI должен показать ошибку.
                            onFinished(isNewUser)
                        }
                }
                .onFailure { remoteException ->
                    Log.e(AppConstants.LOG_TAG, "Failed to load remote user: ${remoteException.message}", remoteException)
                    _uiState.update { it.copy(finalLoading = false, errorMessage = "Failed to load profile: ${remoteException.message}") }
                    // Если не удалось загрузить данные с сервера, сохранять нечего.
                    // Возможно, нужно откатить loginSuccess или показать критическую ошибку.
                }
        }
    }

    fun resetErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun loginSuccess(isUserExists: Boolean) {
        _uiState.value = _uiState.value.copy(loginSuccess = true, isUserExists = isUserExists)
    }

}
