package com.example.chatverse.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatverse.domain.usecase.LoginUseCase
import com.example.chatverse.domain.usecase.SendAuthCodeUseCase
import com.example.chatverse.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sendAuthCodeUseCase: SendAuthCodeUseCase
) : ViewModel() {

    private val _sendAuthCodeState = MutableStateFlow<Result<Unit>?>(null)
    val sendAuthCodeState: StateFlow<Result<Unit>?> = _sendAuthCodeState

    fun sendAuthCode(phone: String) {
        viewModelScope.launch {
            _sendAuthCodeState.value = sendAuthCodeUseCase(phone)
        }
    }
}