package com.example.chatverse.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.ImageLoader

@Composable
fun ProfileAvatar(
    avatarUrl: String,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = avatarUrl,
            imageLoader = imageLoader
        ),
        contentDescription = "Avatar",
        modifier = modifier
            .size(150.dp)
            .clip(CircleShape) // Делает изображение круглым
            .then(Modifier), // Добавляет дополнительные модификаторы, если нужно
        contentScale = ContentScale.Crop // Обрезает изображение по краям для полного заполнения контейнера
    )
}

