package com.example.chatverse.presentation.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.example.chatverse.data.AppConstants
import com.example.chatverse.presentation.ui.components.ProfileAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    avatarUrl:String,
    name: String,
    userName: String,
    phone: String,
    city: String,
    birthDate: String,
    zodiacSign: String,
    about: String,
    onLogout: () -> Unit,
    onEditProfile: () -> Unit,
    imageLoader: ImageLoader
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = onEditProfile) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit Profile")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(modifier = Modifier
                .padding(top = 16.dp)
//                .background(color = MaterialTheme.colorScheme.onSurfaceVariant)
            ) {

                ProfileAvatar(
                    avatarUrl = AppConstants.IMAGE_URL_200 + avatarUrl,
                    imageLoader = imageLoader
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Left
                    )
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Left
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = phone,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Left
                    )
                }
            }
            // Profile Avatar

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Details Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProfileDetailRow(label = "City", value = city)
                    ProfileDetailRow(label = "Birth Date", value = birthDate)
                    ProfileDetailRow(label = "Zodiac Sign", value = zodiacSign)

                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileDetailRow(label = "", value = about)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logout Button
            Button(
                onClick = {
//                    viewModel.logout()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = Color.White
                )
            ) {
//                Icon(Icons.Filled.Logout, contentDescription = "Logout")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout")
            }
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        avatarUrl = "https://via.placeholder.com/150",
        name = "John Doe",
        userName = "JohnDoe007",
        phone = "+1 (123) 456-7890",
        city = "New York",
        birthDate = "1990-01-01",
        zodiacSign = "Capricorn",
        about = "Software Engineer, loves technology and exploring new places.",
        onLogout = { /* No-op for preview */ },
        onEditProfile = { /* No-op for preview */ },
        imageLoader = ImageLoader.Builder(LocalContext.current).build()
    )
}
