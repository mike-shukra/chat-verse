package com.example.chatverse.presentation.navigation

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.ImageLoader
import com.example.chatverse.data.AppConstants
import com.example.chatverse.presentation.ui.login.LoginScreen
import com.example.chatverse.presentation.ui.login.LoginViewModel
import com.example.chatverse.presentation.ui.profile.EditProfileScreen
import com.example.chatverse.presentation.ui.profile.ProfileScreen
import com.example.chatverse.presentation.ui.profile.ProfileViewModel
import com.example.chatverse.presentation.ui.register.RegisterScreen
import com.example.chatverse.presentation.ui.register.RegisterViewModel

@Composable
fun AppNavigator(
    imageLoader: ImageLoader,
    profileViewModel: ProfileViewModel,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            val uiState by loginViewModel.uiState.collectAsState()

            val snackbarHostState = remember { SnackbarHostState() }

            LoginScreen(
                uiState = uiState,
                onSendAuthCode = { loginViewModel.sendAuthCode() },
                onCheckAuthCode = { loginViewModel.checkAuthCode() },
                onCountrySelected = { loginViewModel.onCountrySelected(it) },
                onPhoneNumberChange = { loginViewModel.onPhoneNumberChange(it) },
                onLoginSuccess = { phone, isUserExist ->

                    if (!isUserExist) {
                        navController.navigate("register/$phone") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        loginViewModel.saveUser { success, message ->

                            if (success) {
                                profileViewModel.loadUserProfileDB { successLoad, messageLoad ->

                                    if (successLoad) {
                                        navController.navigate("profile") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    } else {
                                        Log.d(AppConstants.LOG_TAG, "AppNavigator - LoginScreen - saveUser Error: $messageLoad")
                                    }
                                }
                            } else {
                                Log.d(AppConstants.LOG_TAG, "AppNavigator - LoginScreen - saveUser Error: $message")
                            }
                        }
                    }
                },
                onAuthCodeChange = { loginViewModel.onAuthCodeChange(it) },
                onErrorMessage = { errorMessage ->
                    // Логика обработки ошибки
                },
                snackbarHostState = snackbarHostState
            )
        }
        composable("register/{phone}",
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
                avatarUrl = profileViewModel.avatarUrl.value,
                name = profileViewModel.name.value,
                userName = profileViewModel.userName.value,
                phone = profileViewModel.phone.value,
                city = profileViewModel.city.value,
                birthDate = profileViewModel.birthDate.value,
                zodiacSign = profileViewModel.zodiacSign.value,
                about = profileViewModel.about.value,

                onLogout = {
                    // Логика выхода
                },
                onEditProfile = {
                    val name = profileViewModel.name.value
                    val userName = profileViewModel.userName.value
                    val phone = profileViewModel.phone.value
                    val city = profileViewModel.city.value
                    val birthDate = profileViewModel.birthDate.value
                    val about = profileViewModel.about.value

                    navController.navigate(
                        "edit_profile/$name/$userName/$phone/$city/$birthDate/$about"
                    )
                },
                imageLoader = imageLoader
            )
        }

        composable(
            route = "edit_profile/{name}/{userName}/{phone}/{city}/{birthDate}/{about}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("userName") { type = NavType.StringType },
                navArgument("phone") { type = NavType.StringType },
                navArgument("city") { type = NavType.StringType },
                navArgument("birthDate") { type = NavType.StringType },
                navArgument("about") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            val city = backStackEntry.arguments?.getString("city") ?: ""
            val birthDate = backStackEntry.arguments?.getString("birthDate") ?: ""
            val about = backStackEntry.arguments?.getString("about") ?: ""

            EditProfileScreen(
                name = name,
                userName = userName,
                phone = phone,
                city = city,
                birthDate = birthDate,
                about = about,
                onBack = { navController.popBackStack() },
                onSave = { updatedName, updatedUserName,updatedPhone, updatedCity, updatedBirthDate, updatedAbout ->
                    profileViewModel.updateProfile(
                        updatedName, updatedUserName, updatedPhone, updatedCity, updatedBirthDate, updatedAbout
                    )
                    navController.popBackStack()
                }
            )
        }
    }
}