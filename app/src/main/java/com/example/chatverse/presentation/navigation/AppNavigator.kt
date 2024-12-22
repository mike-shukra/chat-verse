package com.example.chatverse.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.ImageLoader
import com.example.chatverse.presentation.ui.chats.ChatMessagesScreen
import com.example.chatverse.presentation.ui.chats.ChatsScreen
import com.example.chatverse.presentation.ui.login.LoginScreen
import com.example.chatverse.presentation.ui.login.LoginViewModel
import com.example.chatverse.presentation.ui.profile.ProfileScreen
import com.example.chatverse.presentation.ui.profile.ProfileViewModel
import com.example.chatverse.presentation.ui.register.RegisterScreen
import com.example.chatverse.presentation.ui.register.RegisterViewModel

sealed class BottomNavItem(val route: String, val label: String, val icon: @Composable () -> Unit) {
    object Profile : BottomNavItem("profile", "Profile", { Icon(Icons.Default.Person, contentDescription = "Profile") })
    object Chats : BottomNavItem("chats", "Chats", { Icon(Icons.Default.MailOutline, contentDescription = "Chats") })
    // Add more items here when needed
}


@Composable
fun AppNavigator(
    imageLoader: ImageLoader,
    profileViewModel: ProfileViewModel,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
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
                                    profileViewModel.loadUserProfileDB { successLoad, _ ->
                                        if (successLoad) {
                                            navController.navigate("profile") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    onAuthCodeChange = { loginViewModel.onAuthCodeChange(it) },
                    onErrorMessage = { },
                    snackbarHostState = snackbarHostState
                )
            }

            composable(
                "register/{phone}",
                arguments = listOf(navArgument("phone") { type = NavType.StringType })
            ) { backStackEntry ->
                val phone = backStackEntry.arguments?.getString("phone") ?: ""
                RegisterScreen(
                    phone = phone,
                    onRegistrationSuccess = {
                        navController.navigate("profile") {
                            popUpTo("register/{phone}") { inclusive = true }
                        }
                    },
                    registerUser = { registerInDto, callback ->
                        registerViewModel.registerUser(registerInDto, callback)
                    },
                    onBack = { navController.popBackStack() }
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
                    onLogout = { },
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

            composable("chats") {
                ChatsScreen(
                    onChatClick = { chatId ->
                        navController.navigate("chat_messages/$chatId")
                    }
                )
            }

            composable(
                route = "chat_messages/{chatId}",
                arguments = listOf(navArgument("chatId") { type = NavType.StringType })
            ) { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                ChatMessagesScreen(
                    chatId = chatId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Profile,
        BottomNavItem.Chats
    )
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = item.icon,
                label = { Text(item.label) }
            )
        }
    }
}
