package com.example.chatverse.presentation.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    // Начальные значения из навигации (или для Preview)
    initialName: String,
    initialPhone: String,
    initialCity: String,
    initialBirthDate: String,
    initialAbout: String,
    profileViewModel: ProfileViewModel = hiltViewModel(), // Получаем ViewModel
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit // Коллбэк для навигации после успешного сохранения
) {
    /*
    Используем состояния из ViewModel как источник правды, но позволяем локальное редактирование.
    Если ViewModel уже имеет данные (например, после loadUserProfileDB), они будут использованы.
    Если мы передали initial* значения (например, для нового пользователя),
    то в composable NavHost мы должны вызвать profileViewModel.setProfileDetails(...) ПЕРЕД этим экраном.
    */
    var editableUserName by remember { mutableStateOf(profileViewModel.name.value.takeIf { it.isNotEmpty() } ?: initialName) }
    var editablePhone by remember { mutableStateOf(profileViewModel.phone.value.takeIf { it.isNotEmpty() } ?: initialPhone) }
    var editableCity by remember { mutableStateOf(profileViewModel.city.value.takeIf { it.isNotEmpty() } ?: initialCity) }
    var editableBirthDate by remember { mutableStateOf(profileViewModel.birthDate.value.takeIf { it.isNotEmpty() } ?: initialBirthDate) }
    var editableAbout by remember { mutableStateOf(profileViewModel.about.value.takeIf { it.isNotEmpty() } ?: initialAbout) }

    val updateInProgress by profileViewModel.updateInProgress
    val updateError by profileViewModel.updateError
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(updateError) {
        updateError?.let {
            snackbarHostState.showSnackbar(it)
            profileViewModel.clearUpdateError() // Сбрасываем ошибку после показа
        }
    }

    // Подписываемся на изменения в ViewModel, если они происходят из другого источника (маловероятно для этих полей здесь)
    // LaunchedEffect(profileViewModel.name.value) { editableUserName = profileViewModel.name.value }
    // ... и так далее для других полей, если это необходимо.
    // В данном случае, так как редактирование локальное, а ViewModel обновляется только при сохранении,
    // это может быть излишним. Но если бы ViewModel могла обновиться извне во время редактирования, это было бы нужно.


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
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
            OutlinedTextField(
                value = editableUserName,
                onValueChange = { editableUserName = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !updateInProgress
            )

            OutlinedTextField(
                value = editablePhone,
                onValueChange = { editablePhone = it },
                label = { Text("Phone") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                enabled = !updateInProgress
                // Примечание: UserUpdateDto не содержит поле 'phone'. Если телефон нужно обновлять,
                // то DTO или метод API должны это поддерживать.
                // Пока что это поле будет редактироваться локально, но не сохранится через текущий updateProfile.
            )

            OutlinedTextField(
                value = editableCity,
                onValueChange = { editableCity = it },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !updateInProgress
            )

            OutlinedTextField(
                value = editableBirthDate,
                onValueChange = { editableBirthDate = it },
                label = { Text("Birth Date (YYYY-MM-DD)") },
                // Здесь можно добавить DatePickerDialog или маску ввода
                modifier = Modifier.fillMaxWidth(),
                enabled = !updateInProgress
            )

            OutlinedTextField(
                value = editableAbout,
                onValueChange = { editableAbout = it },
                label = { Text("About") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                enabled = !updateInProgress
            )

            Spacer(modifier = Modifier.height(8.dp)) // Меньший отступ перед кнопкой

            if (updateInProgress) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(
                    onClick = {
                        profileViewModel.updateProfile(
                            newName = editableUserName,
                            newPhone = editablePhone, // Передаем, но ViewModel должна решить, что с ним делать
                            newCity = editableCity,
                            newBirthDate = editableBirthDate,
                            newAbout = editableAbout,
                            onSuccess = {
                                onSaveSuccess() // Вызываем коллбэк для навигации
                            },
                            onError = { /* Ошибка уже обрабатывается через snackbar */ }
                        )
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
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileScreenPreview() {
    // Для Preview мы не можем легко предоставить Hilt ViewModel,
    // поэтому передаем пустые лямбды и начальные значения.
    // В реальном приложении ViewModel будет предоставлена через hiltViewModel().
    EditProfileScreen(
        initialName = "John Doe",
        initialPhone = "+1 (123) 456-7890",
        initialCity = "New York",
        initialBirthDate = "1990-01-01",
        initialAbout = "Software Engineer...",
        // profileViewModel = viewModel(), // Это вызовет ошибку в Preview без настройки Hilt
        onBack = { /* No-op */ },
        onSaveSuccess = { /* No-op */ }
    )
}