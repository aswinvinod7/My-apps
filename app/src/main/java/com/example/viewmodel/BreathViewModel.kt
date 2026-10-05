package com.example.viewmodel

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AudioCueManager
import com.example.model.BreathPhase
import com.example.model.BreathPreset
import com.example.model.BreathUiState
import com.example.model.TransitionCue
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class BreathViewModel(application: Application) : AndroidViewModel(application) {

    private val audioCueManager = AudioCueManager(application)

    private val _uiState = MutableStateFlow(BreathUiState())
    val uiState: StateFlow<BreathUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun selectPreset(preset: BreathPreset) {
        if (preset == BreathPreset.CUSTOM) {
            _uiState.update { it.copy(breathType = BreathPreset.CUSTOM) }
            return
        }

        _uiState.update { current ->
            current.copy(
                breathType = preset,
                inhaleSeconds = preset.inhale ?: current.inhaleSeconds,
                holdInhaleSeconds = preset.holdInhale ?: current.holdInhaleSeconds,
                exhaleSeconds = preset.exhale ?: current.exhaleSeconds,
                holdExhaleSeconds = preset.holdExhale ?: current.holdExhaleSeconds
            )
        }
    }

    fun updateInhale(value: Float) {
        _uiState.update {
            it.copy(
                breathType = BreathPreset.CUSTOM,
                inhaleSeconds = value.coerceAtLeast(0f)
            )
        }
    }

    fun updateHoldInhale(value: Float) {
        _uiState.update {
            it.copy(
                breathType = BreathPreset.CUSTOM,
                holdInhaleSeconds = value.coerceAtLeast(0f)
            )
        }
    }

    fun updateExhale(value: Float) {
        _uiState.update {
            it.copy(
                breathType = BreathPreset.CUSTOM,
                exhaleSeconds = value.coerceAtLeast(0f)
            )
        }
    }

    fun updateHoldExhale(value: Float) {
        _uiState.update {
            it.copy(
                breathType = BreathPreset.CUSTOM,
                holdExhaleSeconds = value.coerceAtLeast(0f)
            )
        }
    }

    fun updateCycles(text: String) {
        _uiState.update { it.copy(cyclesText = text) }
    }

    fun updateCueType(cue: TransitionCue) {
        _uiState.update { it.copy(cueType = cue) }
    }

    fun toggleStartStop() {
        if (_uiState.value.isRunning) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    private fun buildPhases(state: BreathUiState): List<BreathPhase> {
        val phases = mutableListOf<BreathPhase>()
        if (state.inhaleSeconds > 0f) {
            phases.add(BreathPhase("Inhale", state.inhaleSeconds))
        }
        if (state.holdInhaleSeconds > 0f) {
            phases.add(BreathPhase("Hold", state.holdInhaleSeconds))
        }
        if (state.exhaleSeconds > 0f) {
            phases.add(BreathPhase("Exhale", state.exhaleSeconds))
        }
        if (state.holdExhaleSeconds > 0f) {
            phases.add(BreathPhase("Hold", state.holdExhaleSeconds))
        }
        return phases
    }

    private fun startTimer() {
        val currentState = _uiState.value
        val phases = buildPhases(currentState)
        if (phases.isEmpty()) return

        val cycleVal = currentState.cyclesText.trim()
        val parsedMax = if (cycleVal == "∞" || cycleVal.isEmpty()) {
            null
        } else {
            cycleVal.toIntOrNull()
        }

        timerJob?.cancel()

        _uiState.update {
            it.copy(
                isRunning = true,
                currentCycle = 1,
                maxCycles = parsedMax,
                currentPhaseName = phases[0].name,
                currentPhaseElapsedSeconds = 0.0f,
                progressPercent = 0.0f
            )
        }

        // Trigger cue for the first phase
        triggerCue(phases[0].name, currentState.cueType)

        timerJob = viewModelScope.launch {
            var phaseIdx = 0
            var currentCycle = 1
            var phaseStartRealtime = SystemClock.elapsedRealtime()

            while (isActive) {
                val now = SystemClock.elapsedRealtime()
                val currentPhase = phases[phaseIdx]
                val elapsedSec = ((now - phaseStartRealtime) / 1000.0).toFloat()
                val progress = ((elapsedSec / currentPhase.durationSeconds) * 100f).coerceIn(0f, 100f)

                _uiState.update {
                    it.copy(
                        currentPhaseName = currentPhase.name,
                        currentPhaseElapsedSeconds = elapsedSec,
                        progressPercent = progress,
                        currentCycle = currentCycle
                    )
                }

                if (elapsedSec >= currentPhase.durationSeconds) {
                    phaseIdx++
                    if (phaseIdx >= phases.size) {
                        phaseIdx = 0
                        currentCycle++
                        if (parsedMax != null && currentCycle > parsedMax) {
                            stopTimer()
                            return@launch
                        }
                    }

                    val nextPhase = phases[phaseIdx]
                    val cue = _uiState.value.cueType
                    triggerCue(nextPhase.name, cue)
                    phaseStartRealtime = SystemClock.elapsedRealtime()
                }

                delay(16) // ~60fps smooth progression matching requestAnimationFrame
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        audioCueManager.stop()

        _uiState.update {
            it.copy(
                isRunning = false,
                currentPhaseElapsedSeconds = 0.0f,
                progressPercent = 0.0f
            )
        }
    }

    private fun triggerCue(phaseName: String, cueType: TransitionCue) {
        when (cueType) {
            TransitionCue.VOICE -> audioCueManager.speak(phaseName)
            TransitionCue.CHIME -> audioCueManager.playChime()
            TransitionCue.NONE -> Unit
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioCueManager.release()
    }
}
