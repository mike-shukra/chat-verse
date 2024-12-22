package com.example.chatverse.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.chatverse.R

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1976D2), // Стандартный синий
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1565C0), // Более тёмный синий
    onPrimaryContainer = Color.White,
    secondary = Color(0xFFFBC02D), // Тёмно-жёлтый
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFF57F17), // Ещё более тёмный жёлтый
    onSecondaryContainer = Color.Black,
    background = Color(0xFF303030), // Тёмно-серый
    surface = Color(0xFF424242), // Светло-серый
    onSurface = Color.White,
    onSurfaceVariant = Color(0xFFFBC02D), // Тёмно-жёлтый
    surfaceVariant = Color(0xFF616161), // Цвет для кнопок
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1976D2), // Стандартный синий
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBBDEFB), // Светлый синий
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFFFBC02D), // Тёмно-жёлтый
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFF57F17), // Ещё более тёмный жёлтый
    onSecondaryContainer = Color.Black,
    background = Color(0xFFC9C9C9), // Светлый серый
    surface = Color(0xFFFFFFFF), // Белый
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFEEEEEE), // Цвет для кнопок
    onSurfaceVariant = Color(0xFFF57F17) // Более тёмный жёлтый для кнопок
)

// Шрифт для текста
val robotoFontFamily = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_light, FontWeight.Light),
)

// Определение типографики с классическими цветами
val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = Color.Black // Используем стандартный чёрный для заголовков
    ),
    headlineMedium = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        color = Color.Black // Используем стандартный чёрный для заголовков
    ),
    bodyLarge = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.Black // Стандартный чёрный для основного текста
    ),
    labelSmall = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        color = Color.Gray // Используем серый для малых лейблов
    )
)

@Composable
fun ChatverseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
