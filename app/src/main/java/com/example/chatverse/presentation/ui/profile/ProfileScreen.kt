package com.example.chatverse.presentation.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import com.example.chatverse.presentation.ui.components.ProfileAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    imageLoader: ImageLoader
) {
    val avatarUrl = viewModel.avatarUrl.value
    val phone = viewModel.phone.value
    val city = viewModel.city.value
    val birthDate = viewModel.birthDate.value
    val zodiacSign = viewModel.zodiacSign.value
    val about = viewModel.about.value

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
            if (!avatarUrl.equals("")) {
                ProfileAvatar(
                    avatarUrl = avatarUrl,
                    imageLoader = imageLoader
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Phone: $phone", style = MaterialTheme.typography.bodyMedium)
            Text(text = "City: $city", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Birth Date: $birthDate", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Zodiac: $zodiacSign", style = MaterialTheme.typography.bodyMedium)
            Text(text = "About: $about", style = MaterialTheme.typography.bodyMedium)
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
