package com.rotarola.portafolio_kotlin.presentation.view.atoms

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun VoiceButtonAtom(
    isListening: Boolean,
    isSpeaking: Boolean,
    isProcessing: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.3f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val buttonColor = when {
        isListening -> Color(0xFFE53935)
        isSpeaking -> Color(0xFF1E88E5)
        isProcessing -> Color(0xFFFB8C00)
        else -> MaterialTheme.colorScheme.primary
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        if (isListening) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .scale(scale)
                    .background(
                        color = Color(0xFFE53935).copy(alpha = 0.3f),
                        shape = CircleShape
                    )
            )
        }
        IconButton(
            onClick = onClick,
            enabled = !isProcessing && !isSpeaking,
            modifier = Modifier
                .size(64.dp)
                .background(color = buttonColor, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(
                    id = if (isListening)
                        android.R.drawable.ic_media_pause
                    else
                        android.R.drawable.ic_btn_speak_now
                ),
                contentDescription = when {
                    isListening -> "Stop listening"
                    isSpeaking -> "Bot is speaking"
                    isProcessing -> "Processing..."
                    else -> "Start speaking"
                },
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}