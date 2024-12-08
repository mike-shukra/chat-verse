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
import coil.ImageLoader
import com.example.chatverse.data.AppConstants
import com.example.chatverse.presentation.ui.login.LoginScreen
import com.example.chatverse.presentation.ui.main.MainScreen
import com.example.chatverse.presentation.ui.profile.ProfileScreen
import com.example.chatverse.presentation.ui.register.RegisterScreen
import com.example.chatverse.presentation.ui.theme.ChatverseTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatverseTheme {
                AppNavigator(imageLoader = imageLoader)
            }
        }
    }
}

@Composable
fun AppNavigator(imageLoader: ImageLoader) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { phone, isUserExist ->
                    Log.d(AppConstants.LOG_TAG, "LoginScreen - onLoginSuccess")
                    if (!isUserExist) {
                        navController.navigate("register/$phone") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("profile") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            )
        }
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
            ProfileScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                },
                imageLoader = imageLoader
            )
        }
    }
}

