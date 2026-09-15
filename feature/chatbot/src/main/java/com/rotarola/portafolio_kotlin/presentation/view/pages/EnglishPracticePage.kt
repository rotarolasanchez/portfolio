package com.rotarola.portafolio_kotlin.presentation.view.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.rotarola.portafolio_kotlin.presentation.state.PracticeMode
import com.rotarola.portafolio_kotlin.presentation.view.organisms.VoiceChatOrganism
import com.rotarola.portafolio_kotlin.presentation.viewmodel.EnglishPracticeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnglishPracticePage(
    viewModel: EnglishPracticeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("🇺🇸 English Practice") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        VoiceChatOrganism(
            messages = uiState.messages,
            isListening = uiState.isListening,
            isSpeaking = uiState.isSpeaking,
            isProcessing = uiState.isProcessing,
            currentTranscription = uiState.currentTranscription,
            practiceMode = uiState.practiceMode,
            error = uiState.error,
            onMicClick = {
                if (uiState.isListening) viewModel.stopListening()
                else viewModel.startListening()
            },
            onChangePracticeMode = { mode: PracticeMode ->
                viewModel.changePracticeMode(mode)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}