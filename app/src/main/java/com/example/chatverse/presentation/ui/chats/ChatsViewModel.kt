package com.example.chatverse.presentation.ui.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Модель данных для UI, может совпадать с Chat из ChatsScreen или быть отдельной
data class ChatUiState(
    val id: String,
    val name: String,
    val lastMessage: String,
    val avatarUrl: String? = null // Добавим поле для аватара, если понадобится
)

// Состояние экрана чатов
data class ChatsScreenUiState(
    val isLoading: Boolean = false,
    val chats: List<ChatUiState> = emptyList(),
    val errorMessage: String? = null
)

// Предварительная версия ViewModel
class ChatsViewModel : ViewModel() { // Если будете использовать Hilt, добавьте @HiltViewModel и @Inject constructor

    private val _uiState = MutableStateFlow(ChatsScreenUiState(isLoading = true))
    val uiState: StateFlow<ChatsScreenUiState> = _uiState.asStateFlow()

    init {
        loadChats()
    }

    fun loadChats() {
        viewModelScope.launch {
            _uiState.value = ChatsScreenUiState(isLoading = true)
            // Имитация загрузки данных
            kotlinx.coroutines.delay(1000) // Искусственная задержка

            // В реальном приложении здесь будет запрос к репозиторию для получения чатов
            // Например: val result = chatRepository.getChats()
            // и обработка result.success или result.error

            val mockChats = listOf(
                ChatUiState(id = "1", name = "John Doe", lastMessage = "Hey, how are you?"),
                ChatUiState(id = "2", name = "Jane Smith", lastMessage = "Let's catch up!"),
                ChatUiState(id = "3", name = "Alice", lastMessage = "See you tomorrow!"),
                ChatUiState(id = "4", name = "Bob The Builder", lastMessage = "Can we fix it?")
            )

            _uiState.value = ChatsScreenUiState(isLoading = false, chats = mockChats)
            // Пример обработки ошибки:
            // _uiState.value = ChatsScreenUiState(isLoading = false, errorMessage = "Failed to load chats")
        }
    }

    // Сюда можно будет добавить методы для отправки сообщений, создания новых чатов и т.д.
}