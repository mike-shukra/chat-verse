package com.example.chatverse.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatverse.presentation.ui.login.LoginScreen
import com.example.chatverse.presentation.ui.main.MainScreen
import com.example.chatverse.presentation.ui.profile.ProfileScreen
import com.example.chatverse.presentation.ui.register.RegisterScreen
import com.example.chatverse.presentation.ui.theme.MyProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyProjectTheme {
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        // Удаляем экран авторизации из стека, чтобы пользователь не мог вернуться
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
//        composable("login") {
//            LoginScreen(
//                onLoginSuccess = { isRegistered ->
//                    if (isRegistered) {
//                        // Если пользователь уже зарегистрирован, переходим в профиль
//                        navController.navigate("profile") {
//                            popUpTo("login") { inclusive = true }
//                        }
//                    } else {
//                        // Если нет, перенаправляем на регистрацию
//                        navController.navigate("register") {
//                            popUpTo("login") { inclusive = true }
//                        }
//                    }
//                }
//            )
//        }
        composable("register") {
            RegisterScreen(
                onRegistrationSuccess = {
                    navController.navigate("profile") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
        composable("profile") {
            ProfileScreen()
        }
    }
}

