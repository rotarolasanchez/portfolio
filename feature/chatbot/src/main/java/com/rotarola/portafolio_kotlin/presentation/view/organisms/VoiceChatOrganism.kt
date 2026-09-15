package com.rotarola.portafolio_kotlin.presentation.view.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rotarola.portafolio_kotlin.presentation.state.PracticeMode
import com.rotarola.portafolio_kotlin.presentation.state.VoiceMessage
import com.rotarola.portafolio_kotlin.presentation.view.atoms.VoiceButtonAtom

@Composable
fun VoiceChatOrganism(
    messages: List<VoiceMessage>,
    isListening: Boolean,
    isSpeaking: Boolean,
    isProcessing: Boolean,
    currentTranscription: String,
    practiceMode: PracticeMode,
    error: String?,
    onMicClick: () -> Unit,
    onChangePracticeMode: (PracticeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        PracticeModeSelector(currentMode = practiceMode, onModeSelected = onChangePracticeMode)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message -> VoiceMessageBubble(message = message) }
        }
        if (currentTranscription.isNotEmpty()) {
            Text(
                text = "🎙️ $currentTranscription",
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
        error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 4.dp))
        }
        Text(
            text = when {
                isListening -> "🔴 Listening... speak now"
                isSpeaking -> "🔵 Bot is speaking..."
                isProcessing -> "🟠 Processing your message..."
                else -> "🎤 Tap the mic to start speaking"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 8.dp)
        )
        VoiceButtonAtom(
            isListening = isListening,
            isSpeaking = isSpeaking,
            isProcessing = isProcessing,
            onClick = onMicClick,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun PracticeModeSelector(currentMode: PracticeMode, onModeSelected: (PracticeMode) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        PracticeMode.entries.forEach { mode ->
            FilterChip(
                selected = currentMode == mode,
                onClick = { onModeSelected(mode) },
                label = {
                    Text(text = when (mode) {
                        PracticeMode.FREE_CONVERSATION -> "💬 Chat"
                        PracticeMode.TECHNICAL_INTERVIEW -> "💼 Interview"
                        PracticeMode.VOCABULARY_PRACTICE -> "📚 Vocab"
                    }, fontSize = 11.sp)
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun VoiceMessageBubble(message: VoiceMessage) {
    val isUser = message.isFromUser
    val parts = message.text.split("|")
    val mainText = parts.firstOrNull()?.trim() ?: message.text
    val correction = if (parts.size > 1) parts[1].trim() else message.correction
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = if (isUser) 16.dp else 4.dp, bottomEnd = if (isUser) 4.dp else 16.dp)
                )
                .padding(12.dp)
        ) {
            Column {
                Text(text = if (isUser) "🧑 You" else "🤖 English Coach", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = mainText, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                correction?.let {
                    if (it.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "✏️ $it", style = MaterialTheme.typography.bodySmall, color = Color(0xFF2E7D32), fontStyle = FontStyle.Italic)
                    }
                }
            }
        }
    }
}