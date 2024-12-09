package com.example.chatverse.presentation.ui.login

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatverse.data.AppConstants
import com.example.chatverse.presentation.ui.components.CountryPicker
import com.example.chatverse.presentation.ui.components.PhoneNumberInput
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onSendAuthCode: () -> Unit,
    onCheckAuthCode: () -> Unit,
    onCountrySelected: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onLoginSuccess: (String, Boolean) -> Unit,
    onAuthCodeChange: (String) -> Unit,
    onErrorMessage: (String) -> Unit,
    snackbarHostState: SnackbarHostState
) {

    val authCodeFocusRequester = remember { FocusRequester() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Login", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Учет paddingValues
                .padding(16.dp), // Дополнительные отступы
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp), // Отступы от краёв экрана
                verticalAlignment = Alignment.CenterVertically, // Выравнивание по вертикали
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Расстояние между элементами
            ) {
                CountryPicker(
                    currentRegion = uiState.countries.find { it.second == uiState.countryCode }?.first ?: "US",
                    onCountrySelected = { onCountrySelected(it) }
                )
                PhoneNumberInput(
                    phoneNumber = uiState.phoneNumber,
                    onPhoneNumberChange = { onPhoneNumberChange(it) },
                    countryCode = uiState.countryCode,
                    modifier = Modifier.weight(1f) // Элемент растягивается, занимая оставшееся пространство
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка отправки кода
            Button(
                onClick = { onSendAuthCode() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Send Code", style = MaterialTheme.typography.bodyLarge
                    .copy(color = MaterialTheme.colorScheme.onPrimary))
            }
            Spacer(modifier = Modifier.height(16.dp))


            // Поле ввода кода авторизации
            OutlinedTextField(
                value = uiState.authCode,
                onValueChange = { onAuthCodeChange(it) },
                label = { Text("Auth Code", style = MaterialTheme.typography.labelLarge) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(authCodeFocusRequester)
            )
            Spacer(modifier = Modifier.height(16.dp))


            // Кнопка проверки кода
            Button(
                onClick = { onCheckAuthCode() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Check Code", style = MaterialTheme.typography.bodyLarge
                    .copy(color = MaterialTheme.colorScheme.onPrimary))
            }

            if (uiState.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }

            uiState.errorMessage?.let { error ->
                Log.d(AppConstants.LOG_TAG, "LoginScreen - errorMessage: $error")
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                LaunchedEffect(error) {
                    snackbarHostState.showSnackbar(error)
                    onErrorMessage(error)
                }
            }

            if (uiState.loginSuccess) {
                LaunchedEffect(Unit) {
                    onLoginSuccess(uiState.phoneNumber, uiState.isUserExists!!)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        authCodeFocusRequester.requestFocus()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    // Заглушка для состояния UI
    val uiState = LoginUiState(
        countries = listOf("US" to "+1", "RU" to "+7"),
        countryCode = "+1",
        phoneNumber = "1234567890",
        authCode = "",
        isLoading = false,
        errorMessage = null,
        loginSuccess = false,
        isUserExists = null
    )

    // Заглушки для обработчиков
    val snackbarHostState = remember { SnackbarHostState() }

    LoginScreen(
        uiState = uiState,
        onSendAuthCode = { /* Логика отправки кода */ },
        onCheckAuthCode = { /* Логика проверки кода */ },
        onCountrySelected = { /* Логика выбора страны */ },
        onPhoneNumberChange = { /* Логика изменения номера телефона */ },
        onLoginSuccess = { phoneNumber, isUserExists -> /* Логика успешного логина */ },
        onAuthCodeChange = { /* Логика изменения кода авторизации */ },
        onErrorMessage = { /* Логика обработки ошибки */ },
        snackbarHostState = snackbarHostState
    )


}


