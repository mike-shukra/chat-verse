package com.example.chatverse.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatverse.presentation.ui.theme.MyProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyProjectTheme {
                Column {
                    Text(text = "Hello, Jetpack Compose!")
                    Button(onClick = { /* Handle click */ }) {
                        Text("Click me")
                    }
                }
            }
        }
    }
}

// Превью темы
@Preview(showBackground = true)
@Composable
fun MyProjectThemePreview() {
    MyProjectTheme {
        Text(text = "Hello, Chatverse!")
    }
}
