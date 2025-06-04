package com.example.chatverse.presentation.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.ImageLoader
import com.example.chatverse.data.AppConstants
import com.example.chatverse.data.TokenManager
import com.example.chatverse.presentation.AppWideViewModel
import com.example.chatverse.presentation.NavigationCommand
import com.example.chatverse.presentation.ui.chats.ChatMessagesScreen
import com.example.chatverse.presentation.ui.chats.ChatsScreen
import com.example.chatverse.presentation.ui.login.LoginScreen
import com.example.chatverse.presentation.ui.login.LoginViewModel
import com.example.chatverse.presentation.ui.profile.EditProfileScreen
import com.example.chatverse.presentation.ui.profile.ProfileScreen
import com.example.chatverse.presentation.ui.profile.ProfileViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

sealed class BottomNavItem(val route: String, val label: String, val icon: @Composable () -> Unit) {
    data object Profile : BottomNavItem("profile", "Profile", { Icon(Icons.Default.Person, contentDescription = "Profile") })
    data object Chats : BottomNavItem("chats", "Chats", { Icon(Icons.Default.MailOutline, contentDescription = "Chats") })
    // Add more items here when needed
}

@Composable
fun AppEntryRoot(
    // Зависимости, которые не являются ViewModel, передаются из MainActivity
    imageLoader: ImageLoader,
    tokenManager: TokenManager,
    // ViewModel получаем через hiltViewModel() внутри или передаем, если нужно специфическое поведение
    appWideViewModel: AppWideViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(), // Получаем здесь
    loginViewModel: LoginViewModel = hiltViewModel()     // Получаем здесь
    // chatsViewModel: ChatsViewModel = hiltViewModel() // Если нужна на этом уровне
) {
    val navController = rememberNavController()

    // --- Логика определения начального состояния и маршрута ---
    var startDestination by remember { mutableStateOf<String?>(null) } // null пока не определено

    LaunchedEffect(key1 = tokenManager, key2 = appWideViewModel) {
        val accessToken = tokenManager.getAccessToken()
        val refreshToken = tokenManager.getRefreshToken()
        Log.d("AppEntryRoot", "Checking tokens: Access - $accessToken, Refresh - $refreshToken")

        if (accessToken != null && refreshToken != null) {
            // Здесь можно добавить более сложную проверку токена, если нужно
            // Например, проверка срока действия или быстрый запрос к /users/me
            // Для простоты пока считаем: если токены есть, пользователь "потенциально" авторизован.
            // Окончательная проверка произойдет при первом запросе к API.
            startDestination = BottomNavItem.Profile.route // Или BottomNavItem.Chats.route
        } else {
            startDestination = "login"
        }
    }
    // --- Конец логики определения начального состояния ---

    // --- Обработка глобальных навигационных команд ---
    LaunchedEffect(key1 = navController, key2 = appWideViewModel) {
        appWideViewModel.navigationCommands.collect { command ->
            Log.d("AppEntryRoot", "Received navigation command: $command")
            when (command) {
                is NavigationCommand.NavigateToLoginAndClearStack -> {
                    // Очищаем токены здесь, если это еще не сделано (например, в TokenAuthenticator)
                    // tokenManager.clearTokens() // Рассмотрите, где лучше это делать
                    navController.navigate("login") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true // Избегаем нескольких копий экрана логина
                    }
                }
                // Можно добавить другие глобальные команды
            }
        }
    }
    // --- Конец обработки глобальных навигационных команд ---


    if (startDestination == null) {
        // Показываем экран загрузки/сплэш, пока определяется начальный маршрут
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return // Выходим, пока startDestination не определен
    }

    // Основной UI приложения
    AppNavigatorScaffold(
        navController = navController,
        startDestination = startDestination!!, // Уверены, что не null после проверки выше
        imageLoader = imageLoader,
        profileViewModel = profileViewModel,
        loginViewModel = loginViewModel,
        appWideViewModel = appWideViewModel, // Для кнопки Logout, если она в ProfileScreen
        tokenManager = tokenManager // Для кнопки Logout, если она в ProfileScreen и очищает токен
    )
}

@Composable
fun AppNavigatorScaffold(
    navController: NavHostController,
    startDestination: String,
    imageLoader: ImageLoader,
    profileViewModel: ProfileViewModel,
    loginViewModel: LoginViewModel,
    appWideViewModel: AppWideViewModel, // Передаем для доступа к logoutUser
    tokenManager: TokenManager // Передаем для очистки токенов при logout
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        BottomNavItem.Profile.route,
        BottomNavItem.Chats.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // В AppNavigatorScaffold в AppEntryRoot.kt

            composable("login") {
                val uiState by loginViewModel.uiState.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }

                LoginScreen(
                    uiState = uiState,
                    onSendAuthCode = { loginViewModel.sendAuthCode() },
                    onCheckAuthCode = { loginViewModel.checkAuthCode() },
                    onCountrySelected = { countryIsoCode -> loginViewModel.onCountrySelected(countryIsoCode) },
                    onPhoneNumberChange = { phoneNumber -> loginViewModel.onPhoneNumberChange(phoneNumber) },
                    onAuthCodeChange = { authCode -> loginViewModel.onAuthCodeChange(authCode) },
                    onAuthCodeVerified = {
                        loginViewModel.completeLoginAndSaveUser { isNewUser ->
                            profileViewModel.loadUserProfileDB { successLoad, _ ->
                                if (successLoad) {
                                    val destination = if (isNewUser) {
                                        val name = profileViewModel.name.value ?: ""
                                        val phone = profileViewModel.phone.value ?: ""
                                        val city = profileViewModel.city.value ?: ""
                                        val birthDate = profileViewModel.birthDate.value ?: ""
                                        val about = profileViewModel.about.value ?: ""
                                        "edit_profile/" +
                                                "${Uri.encode(name)}/" +
                                                "${Uri.encode(phone)}/" +
                                                "${Uri.encode(city)}/" +
                                                "${Uri.encode(birthDate)}/" +
                                                Uri.encode(about)
                                    } else {
                                        BottomNavItem.Profile.route
                                    }
                                    navController.navigate(destination) {
                                        popUpTo("login") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                } else {
                                    Log.e(AppConstants.LOG_TAG, "Failed to load profile from DB after login completion.")
                                    // loginViewModel.showCriticalError("Failed to prepare user profile.")
                                }
                            }
                        }
                    },
                    onErrorMessageShown = {
                        loginViewModel.resetErrorMessage()
                    },
                    snackbarHostState = snackbarHostState
                )
            }

            composable(BottomNavItem.Profile.route) {
                LaunchedEffect(key1 = profileViewModel) { // Используем profileViewModel как ключ
                    // или key1 = Unit, если profileViewModel не меняется
                    if (profileViewModel.name.value.isEmpty()) { // Простая проверка, загружены ли данные
                        Log.d(AppConstants.LOG_TAG, "ProfileScreen: name is empty, calling loadUserProfileDB")
                        profileViewModel.loadUserProfileDB { success, error ->
                            if (!success) {
                                Log.e(AppConstants.LOG_TAG, "ProfileScreen: Failed to load profile from DB: $error")
                                // Показать ошибку, если нужно
                            } else {
                                Log.d(AppConstants.LOG_TAG, "ProfileScreen: Profile loaded successfully from DB.")
                            }
                        }
                    } else {
                        Log.d(AppConstants.LOG_TAG, "ProfileScreen: Profile data already present in ViewModel (or loaded previously).")
                    }
                }
                ProfileScreen(
                    // ... параметры ProfileScreen ...
                    // Используем profileViewModel, который был получен в AppEntryRoot
                    avatarUrl = profileViewModel.avatarUrl.value,
                    name = profileViewModel.name.value,
                    userName = profileViewModel.userName.value,
                    phone = profileViewModel.phone.value,
                    city = profileViewModel.city.value,
                    birthDate = profileViewModel.birthDate.value,
                    zodiacSign = profileViewModel.zodiacSign.value,
                    about = profileViewModel.about.value,
                    imageLoader = imageLoader,
                    onEditProfile = {
                        val name = profileViewModel.name.value ?: ""
                        val phone = profileViewModel.phone.value ?: ""
                        val city = profileViewModel.city.value ?: ""
                        val birthDate = profileViewModel.birthDate.value ?: ""
                        val about = profileViewModel.about.value ?: ""
                        val route = "edit_profile/" +
                                "${Uri.encode(name)}/" +
                                "${Uri.encode(phone)}/" +
                                "${Uri.encode(city)}/" +
                                "${Uri.encode(birthDate)}/" +
                                Uri.encode(about)
                        navController.navigate(route)
                    },
                    onLogout = {
                        // Очистка токенов и навигация на логин через AppWideViewModel
                        profileViewModel.logout()
                        appWideViewModel.logoutUser() // Это вызовет NavigateToLoginAndClearStack
                    }
                )
            }

            composable(
                route = "edit_profile/{name}/{phone}/{city}/{birthDate}/{about}",
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("phone") { type = NavType.StringType },
                    navArgument("city") { type = NavType.StringType; nullable = true },
                    navArgument("birthDate") { type = NavType.StringType; nullable = true },
                    navArgument("about") { type = NavType.StringType; nullable = true }
                )
            ) { backStackEntry ->
                // Получаем ProfileViewModel. Он будет тот же самый экземпляр, что и в ProfileScreen,
                // если они находятся в одном NavGraph (обычно так и есть).
                val profileViewModel: ProfileViewModel = hiltViewModel()

                // Извлекаем аргументы из навигации
                val nameArg = backStackEntry.arguments?.getString("name") ?: ""
                val phoneArg = backStackEntry.arguments?.getString("phone") ?: ""
                val cityArg = backStackEntry.arguments?.getString("city") ?: ""
                val birthDateArg = backStackEntry.arguments?.getString("birthDate") ?: ""
                val aboutArg = backStackEntry.arguments?.getString("about") ?: ""

                // Устанавливаем детали в ViewModel ПЕРЕД тем, как EditProfileScreen будет скомпонован.
                // Это важно, если мы переходим с данными нового пользователя, чтобы экран
                // сразу показал эти данные, а не пустые значения из ViewModel по умолчанию.
                // LaunchedEffect с ключом Unit гарантирует, что это выполнится один раз при входе на экран.
                LaunchedEffect(Unit) {
                    profileViewModel.setProfileDetails(
                        name = nameArg,
                        phone = phoneArg,
                        city = cityArg,
                        birthDate = birthDateArg,
                        about = aboutArg
                    )
                }

                EditProfileScreen(
                    initialName = nameArg, // Передаем для инициализации, если ViewModel еще пуста
                    initialPhone = phoneArg,
                    initialCity = cityArg,
                    initialBirthDate = birthDateArg,
                    initialAbout = aboutArg,
                    profileViewModel = profileViewModel, // Передаем саму ViewModel
                    onBack = { navController.popBackStack() },
                    onSaveSuccess = {
                        // После успешного сохранения, возвращаемся на предыдущий экран (вероятно, ProfileScreen)
                        navController.popBackStack()
                        // Альтернативно, можно навигировать на конкретный экран, если нужно
                        // navController.navigate(BottomNavItem.Profile.route) { popUpTo("edit_profile") { inclusive = true } }
                    }
                )
            }

            composable(BottomNavItem.Chats.route) {
                // ChatsViewModel будет получен внутри ChatsScreen через hiltViewModel()
                ChatsScreen(
                    // viewModel = hiltViewModel(), // неявно, если есть значение по умолчанию
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