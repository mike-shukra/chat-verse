package com.example.chatverse.presentation.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatverse.data.local.model.UserEntity
import com.example.chatverse.data.remote.dto.RegisterInDto
import com.example.chatverse.presentation.ui.profile.ProfileViewModel

@Composable
fun RegisterScreen(
    phone: String,
    onRegistrationSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val viewModel: RegisterViewModel = hiltViewModel()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Номер телефона
                Text(
                    text = "Phone: $phone",
                    style = MaterialTheme.typography.body1
                )

                // Поле ввода имени
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Поле ввода username с валидацией
                OutlinedTextField(
                    value = username,
                    onValueChange = { input ->
                        if (input.matches("^[A-Za-z0-9\\-_]*$".toRegex())) {
                            username = input
                        }
                    },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Only A-Z, a-z, 0-9, -, _") }
                )

                // Кнопка регистрации
                Button(
                    onClick = {
                        isLoading = true
                        val registerInDto = RegisterInDto(phone, name, username)
                        viewModel.registerUser(registerInDto) { success, error ->
                            isLoading = false
                            if (success) {
                                onRegistrationSuccess()
                            } else {
                                errorMessage = error
                                //todo тестовое API не поддерживает создание новых пользователей
                                onRegistrationSuccess()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = name.isNotEmpty() && username.isNotEmpty()
                ) {
                    Text("Register")
                }

                // Ошибка
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}