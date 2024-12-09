package com.example.chatverse.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import com.example.chatverse.presentation.navigation.AppNavigator
import com.example.chatverse.presentation.ui.login.LoginViewModel
import com.example.chatverse.presentation.ui.profile.ProfileViewModel
import com.example.chatverse.presentation.ui.register.RegisterViewModel
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
                val profileViewModel: ProfileViewModel = hiltViewModel()
                val loginViewModel: LoginViewModel = hiltViewModel()
                val registerViewModel: RegisterViewModel = hiltViewModel()
                AppNavigator(
                    imageLoader = imageLoader,
                    profileViewModel = profileViewModel,
                    loginViewModel =  loginViewModel,
                    registerViewModel = registerViewModel
                )
            }
        }
    }
}



