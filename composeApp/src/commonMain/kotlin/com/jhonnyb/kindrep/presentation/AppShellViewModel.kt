package com.jhonnyb.kindrep.presentation

import androidx.lifecycle.ViewModel
import com.jhonnyb.kindrep.navigation.TopLevelTab
import com.jhonnyb.kindrep.platform.AudioCuePlayer
import com.jhonnyb.kindrep.platform.MediaResolver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AppShellUiState(
    val hasAcceptedDisclaimer: Boolean = false,
    val selectedTab: TopLevelTab = TopLevelTab.TODAY,
    val isSafetyModalVisible: Boolean = false,
)

class AppShellViewModel(
    private val audioCuePlayer: AudioCuePlayer,
    private val mediaResolver: MediaResolver,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppShellUiState())
    val uiState: StateFlow<AppShellUiState> = _uiState.asStateFlow()

    fun acceptDisclaimer() {
        _uiState.update { it.copy(hasAcceptedDisclaimer = true) }
    }

    fun selectTab(tab: TopLevelTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun setSafetyModalVisible(visible: Boolean) {
        _uiState.update { it.copy(isSafetyModalVisible = visible) }
    }

    fun previewAudioCue(cueKey: String = "ready") {
        audioCuePlayer.playChime(cueKey)
    }

    fun hasBundledVideo(mediaKey: String): Boolean =
        mediaResolver.resolveLocalVideoUri(mediaKey) != null
}
