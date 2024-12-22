package com.example.chatverse.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CountryPicker(
    currentRegion: String,
    onCountrySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val countries = listOf("US" to "+1", "RU" to "+7", "IN" to "+91") // Список стран и кодов

    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { expanded = true }
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 16.dp,
                    bottom = 8.dp,
                )
        ) {
            // Отображение флага
            Text(
                text = currentRegion,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            CountryFlag(countryCode = currentRegion, modifier = Modifier.padding(end = 8.dp))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            countries.forEach { (country, code) ->
                DropdownMenuItem(
                    text = { Text("$country $code") },
                    onClick = {
                        expanded = false
                        onCountrySelected(code)
                    }
                )
            }
        }
    }
}
