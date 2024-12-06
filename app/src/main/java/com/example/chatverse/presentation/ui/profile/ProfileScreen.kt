package com.example.chatverse.presentation.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatverse.data.remote.dto.RegisterInDto
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val userName by viewModel.userName
    val userFullName by viewModel.userFullName

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") }
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
            Text(
                text = "Welcome, $userFullName!",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Username: $userName",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}
