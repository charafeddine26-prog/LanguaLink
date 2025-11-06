package com.example.langualink.ui.community

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean
)

data class ChatUiState(
    val chatPartnerName: String = "",
    val messages: List<ChatMessage> = emptyList(),
    val replyOptions: List<String> = emptyList()
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private val chatId: String = savedStateHandle.get<String>("chatId")!!

    init {
        loadInitialMessages()
    }

    private fun loadInitialMessages() {
        viewModelScope.launch {
            if (chatId == "antoine") {
                _uiState.update {
                    it.copy(
                        chatPartnerName = "Antoine",
                        messages = listOf(ChatMessage("Bonjour!", isFromUser = false)),
                        replyOptions = listOf("Bonjour", "Bonsoir", "Salut")
                    )
                }
            } else if (chatId == "lea") {
                _uiState.update {
                    it.copy(
                        chatPartnerName = "Léa",
                        messages = listOf(ChatMessage("Comment ça va ?", isFromUser = false)),
                        replyOptions = listOf("Très bien, et toi ?", "Pas mal", "Comme ci, comme ça")
                    )
                }
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    messages = it.messages + ChatMessage(text, isFromUser = true),
                    replyOptions = emptyList()
                )
            }

            delay(1200)

            val replyText = getSimulatedReply(text)
            _uiState.update {
                it.copy(
                    messages = it.messages + ChatMessage(replyText, isFromUser = false)
                )
            }
        }
    }

    private fun getSimulatedReply(userMessage: String): String {
        return if (chatId == "antoine") {
            "Tu as bien dormi?"
        } else {
            "Je vais bien aussi, merci de demander!"
        }
    }
}