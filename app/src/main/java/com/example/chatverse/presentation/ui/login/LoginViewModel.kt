package com.example.chatverse.presentation.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatverse.data.AppConstants
import com.example.chatverse.domain.usecase.CheckAuthCodeUseCase
import com.example.chatverse.domain.usecase.SendAuthCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sendAuthCodeUseCase: SendAuthCodeUseCase,
    private val checkAuthCodeUseCase: CheckAuthCodeUseCase
) : ViewModel() {

    private val _sendAuthCodeState = MutableStateFlow<Result<Unit>?>(null)
    val sendAuthCodeState: StateFlow<Result<Unit>?> = _sendAuthCodeState

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> get() = _uiState

    fun sendAuthCode(phone: String) {
        viewModelScope.launch {
            _sendAuthCodeState.value = try {
                sendAuthCodeUseCase(phone)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun checkAuthCode(phoneNumber: String, authCode: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        Log.i(AppConstants.LOG_TAG, "phoneNumber: $phoneNumber")
        viewModelScope.launch {
            val result = runCatching { checkAuthCodeUseCase(phoneNumber, authCode) }

            result.onSuccess { loginResult ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isUserExists = loginResult.isUserExists,
                    loginSuccess = true
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message
                )
            }
        }
    }

    fun resetErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
