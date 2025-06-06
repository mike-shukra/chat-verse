package com.example.chatverse.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import coil.ImageLoader
import com.example.chatverse.data.TokenManager // Убедитесь, что импорт правильный
import com.example.chatverse.presentation.navigation.AppEntryRoot // Импортируем AppEntryRoot
import com.example.chatverse.presentation.ui.theme.ChatverseTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatverseTheme {
                // ViewModel теперь создаются/получаются внутри AppEntryRoot или ниже по иерархии
                // с помощью hiltViewModel()
                AppEntryRoot(
                    imageLoader = imageLoader,
                    tokenManager = tokenManager
                )
            }
        }
    }
}



