package com.example.chatverse.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import java.util.Locale

@Composable
fun CountryFlag(countryCode: String, modifier: Modifier = Modifier) {
    val flagUrl = "https://flagcdn.com/w40/${countryCode.lowercase(Locale.ROOT)}.png"

    Image(
        painter = rememberAsyncImagePainter(model = flagUrl),
        contentDescription = "$countryCode Flag",
        modifier = modifier.size(40.dp)
    )
}
