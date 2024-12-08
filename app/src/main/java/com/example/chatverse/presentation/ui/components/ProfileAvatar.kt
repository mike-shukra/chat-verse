package com.example.chatverse.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.example.chatverse.data.AppConstants

@Composable
fun ProfileAvatar(
    avatarUrl: String,
    imageLoader: ImageLoader
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = AppConstants.BASE_URL + "media/avatars/200x200/" + avatarUrl,
            imageLoader = imageLoader
        ),
        contentDescription = "Avatar",
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
    )

}
