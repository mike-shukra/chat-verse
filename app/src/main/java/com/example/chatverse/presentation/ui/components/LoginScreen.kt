package com.example.chatverse.presentation.ui.components

import androidx.compose.runtime.Composable

//@Composable
//fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
//    var phoneNumber by remember { mutableStateOf("") }
//    var smsCode by remember { mutableStateOf("") }
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        TextField(
//            value = phoneNumber,
//            onValueChange = { phoneNumber = it },
//            label = { Text("Номер телефона") }
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(
//            value = smsCode,
//            onValueChange = { smsCode = it },
//            label = { Text("Код подтверждения") }
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = { viewModel.sendAuthCode(phoneNumber) }) {
//            Text("Отправить")
//        }
//        Button(onClick = { viewModel.checkAuthCode(phoneNumber, smsCode) }) {
//            Text("Войти")
//        }
//    }
//}
