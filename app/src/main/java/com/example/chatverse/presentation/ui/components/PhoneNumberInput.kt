package com.example.chatverse.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun PhoneNumberInput(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    countryCode: String,
    placeholder: String = "(XXX) XXX-XXXX",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier // Применяем переданный модификатор
            .fillMaxWidth()
            .padding(0.dp), // Отступы внутри компонента
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = countryCode,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 8.dp, top = 8.dp) // Отступ от текста к вводу
        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            modifier = Modifier.weight(1f), // Растяжение TextField
            label = { Text(placeholder, style = MaterialTheme.typography.labelLarge) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
        )
    }
}
