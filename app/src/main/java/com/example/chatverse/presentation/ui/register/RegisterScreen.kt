package com.example.chatverse.presentation.ui.register

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatverse.data.remote.dto.RegisterInDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    phone: String,
    onRegistrationSuccess: () -> Unit,
    registerUser: (RegisterInDto, (Boolean, String?) -> Unit) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Register") },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = { onBack() }) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Phone: $phone",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name", style = MaterialTheme.typography.labelLarge) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = username,
                        onValueChange = { input ->
                            if (input.matches("^[A-Za-z0-9\\-_]*$".toRegex())) {
                                username = input
                            }
                        },
                        label = { Text("Username", style = MaterialTheme.typography.labelLarge) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Only A-Z, a-z, 0-9, -, _") }
                    )
                    Button(
                        onClick = {
                            isLoading = true
                            val registerInDto = RegisterInDto(phone, name, username)
                            registerUser(registerInDto) { success, error ->
                                isLoading = false
                                if (success) {
                                    onRegistrationSuccess()
                                } else {
                                    errorMessage = error
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = name.isNotEmpty() && username.isNotEmpty()
                    ) {
                        Text(
                            "Register",
                            style = MaterialTheme.typography.bodyLarge
                                .copy(color = MaterialTheme.colorScheme.onPrimary)
                        )
                    }
                    errorMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(
        phone = "+1234567890",
        onRegistrationSuccess = {
        },
        registerUser = { registerInDto, callback ->
            callback(true, null)
        },
        onBack = {
        }
    )
}


