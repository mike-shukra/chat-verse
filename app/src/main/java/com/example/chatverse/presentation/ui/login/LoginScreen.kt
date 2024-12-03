package com.example.chatverse.presentation.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel()) {
    var phoneNumber by remember { mutableStateOf("") }
    var authCode by remember { mutableStateOf("") }
    var isCodeSent by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login") },
                elevation = 4.dp
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isCodeSent) {
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.sendAuthCode(phoneNumber)
                        isCodeSent = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Send Code")
                }
            } else {
                TextField(
                    value = authCode,
                    onValueChange = { authCode = it },
                    label = { Text("Auth Code") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.sendAuthCode(authCode)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Verify Code")
                }
            }
        }
    }
}
