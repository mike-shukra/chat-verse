package com.example.chatverse.presentation.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    name: String,
    userName: String,
    phone: String,
    city: String,
    birthDate: String,
    about: String,
    onBack: () -> Unit,
    onSave: (String, String, String, String, String, String) -> Unit
) {
    var editableUserName by remember { mutableStateOf(name) }
    var editableUserFullName by remember { mutableStateOf(userName) }
    var editablePhone by remember { mutableStateOf(phone) }
    var editableCity by remember { mutableStateOf(city) }
    var editableBirthDate by remember { mutableStateOf(birthDate) }
    var editableAbout by remember { mutableStateOf(about) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TextField для редактирования имени
            OutlinedTextField(
                value = editableUserName,
                onValueChange = { editableUserName = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // TextField для редактирования имени
            OutlinedTextField(
                value = editableUserFullName,
                onValueChange = { editableUserFullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // TextField для редактирования номера телефона
            OutlinedTextField(
                value = editablePhone,
                onValueChange = { editablePhone = it },
                label = { Text("Phone") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            // TextField для редактирования города
            OutlinedTextField(
                value = editableCity,
                onValueChange = { editableCity = it },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth()
            )

            // TextField для редактирования даты рождения
            OutlinedTextField(
                value = editableBirthDate,
                onValueChange = { editableBirthDate = it },
                label = { Text("Birth Date") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // TextField для редактирования информации "О себе"
            OutlinedTextField(
                value = editableAbout,
                onValueChange = { editableAbout = it },
                label = { Text("About") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка для сохранения изменений
            Button(
                onClick = {
                    onSave(editableUserName, editableUserFullName, editablePhone, editableCity, editableBirthDate, editableAbout)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Save Changes")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(
        name = "John Doe",
        userName = "JohnDoe007",
        phone = "+1 (123) 456-7890",
        city = "New York",
        birthDate = "1990-01-01",
        about = "Software Engineer, loves technology and exploring new places.",
        onBack = { /* No-op */ },
        onSave = { _, _, _, _, _, _ -> /* No-op */ }
    )
}
