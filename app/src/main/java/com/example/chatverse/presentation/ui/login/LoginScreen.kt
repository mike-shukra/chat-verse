package com.example.chatverse.presentation.ui.login

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatverse.data.AppConstants
import com.example.chatverse.presentation.ui.components.CountryPicker
import com.example.chatverse.presentation.ui.components.PhoneNumberInput
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: (String, Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val authCode = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    var phoneNumber by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("+1") }

    var currentRegion = Locale.getDefault().country

    val countries = listOf(
        "US" to "+1",
        "RU" to "+7",
        "IN" to "+91",
        "FR" to "+33",
        "DE" to "+49"
    )

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
                    currentRegion = currentRegion,
                    onCountrySelected = { newCode ->
                        countryCode = newCode
                        currentRegion = countries.find { it.second == newCode }?.first ?: "US"
                    }
                )
                PhoneNumberInput(
                    phoneNumber = phoneNumber,
                    onPhoneNumberChange = { phoneNumber = it },
                    countryCode = countryCode,
                    modifier = Modifier.weight(1f) // Элемент растягивается, занимая оставшееся пространство
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка отправки кода
            Button(
                onClick = { viewModel.sendAuthCode(countryCode + phoneNumber) },
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
                value = authCode.value,
                onValueChange = { authCode.value = it },
                label = { Text("Auth Code", style = MaterialTheme.typography.labelLarge) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))


            // Кнопка проверки кода
            Button(
                onClick = { viewModel.checkAuthCode(countryCode + phoneNumber, authCode.value) },
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
                    viewModel.resetErrorMessage()
                }
            }

            if (uiState.loginSuccess) {
                LaunchedEffect(Unit) {
                    viewModel.saveUser { success, error ->
                        if (success) {
                            onLoginSuccess(phoneNumber, uiState.isUserExists!!)
                        } else {
                            //TODO
                        }
                    }
                }
            }
        }
    }
}
