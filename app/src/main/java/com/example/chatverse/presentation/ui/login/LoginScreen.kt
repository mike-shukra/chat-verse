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
import com.example.chatverse.data.AppConstants
import com.example.chatverse.presentation.ui.components.CountryPicker
import com.example.chatverse.presentation.ui.components.PhoneNumberInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onSendAuthCode: () -> Unit,
    onCheckAuthCode: () -> Unit,
    onCountrySelected: (String) -> Unit, // Этот String - ISO код страны, например "US"
    onPhoneNumberChange: (String) -> Unit,
    // Измененный параметр: этот коллбэк вызывается, когда uiState.loginSuccess == true
    // Он должен запустить финальные шаги во ViewModel.
    onAuthCodeVerified: () -> Unit,
    onAuthCodeChange: (String) -> Unit,
    onErrorMessageShown: () -> Unit, // Коллбэк для сброса ошибки после показа Snackbar
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
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CountryPicker(
                    // Находим ISO код страны (например, "US") по телефонному коду (например, "+1")
                    // Если uiState.countries это List<Pair<String, String>> где first="US", second="+1"
                    currentRegion = uiState.countries.find { it.second == uiState.countryCode }?.first ?: "US",
                    onCountrySelected = { countryIsoCode -> onCountrySelected(countryIsoCode) }
                )
                PhoneNumberInput(
                    phoneNumber = uiState.phoneNumber,
                    onPhoneNumberChange = onPhoneNumberChange, // Передаем напрямую
                    countryCode = uiState.countryCode,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSendAuthCode, // Передаем напрямую
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && !uiState.finalLoading, // Также блокируем при finalLoading
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Send Code", style = MaterialTheme.typography.bodyLarge
                    .copy(color = MaterialTheme.colorScheme.onPrimary))
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.authCode,
                onValueChange = onAuthCodeChange, // Передаем напрямую
                label = { Text("Auth Code", style = MaterialTheme.typography.labelLarge) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(authCodeFocusRequester),
                enabled = !uiState.isLoading && !uiState.finalLoading // Также блокируем при finalLoading
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onCheckAuthCode, // Передаем напрямую
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && !uiState.finalLoading, // Также блокируем при finalLoading
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Check Code", style = MaterialTheme.typography.bodyLarge
                    .copy(color = MaterialTheme.colorScheme.onPrimary))
            }

            // Общий индикатор загрузки (для send/check code и для finalLoading)
            if (uiState.isLoading || uiState.finalLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }

            uiState.errorMessage?.let { error ->
                Log.d(AppConstants.LOG_TAG, "LoginScreen - errorMessage: $error")
                // Snackbar покажется автоматически, если snackbarHostState используется в Scaffold
                // Этот LaunchedEffect для показа Snackbar и последующего сброса ошибки во ViewModel
                LaunchedEffect(error, snackbarHostState) {
                    snackbarHostState.showSnackbar(
                        message = error,
                        duration = SnackbarDuration.Short
                    )
                    onErrorMessageShown() // Сообщаем ViewModel, что сообщение показано
                }
                // Можно дополнительно отобразить текст ошибки на экране, если нужно
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            // Когда код успешно проверен (loginSuccess = true),
            // запускаем процесс завершения логина во ViewModel.
            if (uiState.loginSuccess) {
                // Используем LaunchedEffect, чтобы onAuthCodeVerified вызвался один раз,
                // когда loginSuccess становится true.
                // Ключ Unit означает, что это запустится при первой композиции, где loginSuccess == true.
                // Если loginSuccess может стать false, а потом снова true, и нужно реагировать каждый раз,
                // то ключом может быть сам loginSuccess (или более сложный ключ, если нужно).
                // Но обычно loginSuccess устанавливается один раз для успешного входа.
                LaunchedEffect(key1 = uiState.loginSuccess) {
                    // Проверяем еще раз, чтобы избежать вызова, если состояние быстро изменилось
                    if (uiState.loginSuccess) {
                        onAuthCodeVerified()
                    }
                }
            }
        }
    }

    // Фокус на поле ввода кода при первом запуске экрана, если код еще не отправлен/проверен
    LaunchedEffect(uiState.authCodeSent) {
        if (uiState.authCodeSent && uiState.authCode.isEmpty()) { // Если код отправлен, но поле еще пустое
            authCodeFocusRequester.requestFocus()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val uiState = LoginUiState(
        countries = listOf("US" to "+1", "RU" to "+7"),
        countryCode = "+1",
        phoneNumber = "1234567890",
        authCode = "1234",
        isLoading = false,
        errorMessage = null, // "Sample error message for preview",
        loginSuccess = false, // true для теста LaunchedEffect
        isUserExists = null,
        authCodeSent = true,
        finalLoading = false
    )
    val snackbarHostState = remember { SnackbarHostState() }

    MaterialTheme { // Обертка в MaterialTheme для Preview
        LoginScreen(
            uiState = uiState,
            onSendAuthCode = { Log.d("Preview", "Send Auth Code") },
            onCheckAuthCode = { Log.d("Preview", "Check Auth Code") },
            onCountrySelected = { Log.d("Preview", "Country Selected: $it") },
            onPhoneNumberChange = { Log.d("Preview", "Phone Changed: $it") },
            onAuthCodeVerified = { Log.d("Preview", "Auth Code Verified, proceeding...") },
            onAuthCodeChange = { Log.d("Preview", "Auth Code Changed: $it") },
            onErrorMessageShown = { Log.d("Preview", "Error message shown, reset.") },
            snackbarHostState = snackbarHostState
        )
    }
}