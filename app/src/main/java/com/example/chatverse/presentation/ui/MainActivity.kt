package com.example.chatverse.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
                onLoginSuccess = { phone ->
                    navController.navigate("register/$phone") {
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
        composable(
            route = "register/{phone}",
            arguments = listOf(navArgument("phone") { type = NavType.StringType })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            RegisterScreen(
                phone = phone,
                onRegistrationSuccess = {
                    navController.navigate("profile") {
                        popUpTo("register/{phone}") { inclusive = true }
                    }
                }
            )
        }
        composable("profile") {
            ProfileScreen()
        }
    }
}

