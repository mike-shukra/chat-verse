package com.example.chatverse.presentation.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatverse.data.AppConstants
import com.example.chatverse.domain.repository.UserRepository
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
    private val checkAuthCodeUseCase: CheckAuthCodeUseCase,
    private val repository: UserRepository
) : ViewModel() {

//    private val _sendAuthCodeState = MutableStateFlow<Result<Unit>?>(null)
//    val sendAuthCodeState: StateFlow<Result<Unit>?> = _sendAuthCodeState

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phoneNumber)
    }

    fun onAuthCodeChange(authCode: String) {
        _uiState.value = _uiState.value.copy(authCode = authCode)
    }

    fun onCountrySelected(countryCode: String) {
        _uiState.value = _uiState.value.copy(countryCode = countryCode)
    }

    fun checkAuthCode() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        Log.d(AppConstants.LOG_TAG, "LoginViewModel - checkAuthCode - phoneNumber: ${uiState.value.phoneNumber}")
        viewModelScope.launch {
            val result = runCatching { checkAuthCodeUseCase(uiState.value.countryCode + uiState.value.phoneNumber, uiState.value.authCode) }

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

    fun sendAuthCode() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            val result = runCatching { sendAuthCodeUseCase(uiState.value.countryCode + uiState.value.phoneNumber) }
            result.onSuccess { loginResult ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
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

    fun loginSuccess(isUserExists: Boolean) {
        _uiState.value = _uiState.value.copy(loginSuccess = true, isUserExists = isUserExists)
    }

    fun saveUser(onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val remoteUser = repository.loadRemoteUser()
            repository.saveUserProfileInDB(remoteUser, onResult)
        }
    }
}
