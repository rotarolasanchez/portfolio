package com.rotarola.portafolio_kotlin.presentation.state

import java.util.UUID

data class EnglishPracticeUiState(
    val messages: List<VoiceMessage> = emptyList(),
    val isListening: Boolean = false,
    val isSpeaking: Boolean = false,
    val isProcessing: Boolean = false,
    val currentTranscription: String = "",
    val error: String? = null,
    val practiceMode: PracticeMode = PracticeMode.FREE_CONVERSATION
)

data class VoiceMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isFromUser: Boolean,
    val correction: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class PracticeMode {
    FREE_CONVERSATION,
    TECHNICAL_INTERVIEW,
    VOCABULARY_PRACTICE
}