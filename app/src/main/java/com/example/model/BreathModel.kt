package com.example.model

enum class BreathPreset(
    val id: String,
    val displayName: String,
    val inhale: Float?,
    val holdInhale: Float?,
    val exhale: Float?,
    val holdExhale: Float?
) {
    RELAXING("relaxing", "Relaxing Breath", 4f, 7f, 8f, 0f),
    BOX("box", "Box Breath", 4f, 4f, 4f, 4f),
    EASY_RELAXING("easyRelaxing", "Easy Relaxing Breath", 4f, 0f, 8f, 0f),
    EASY_ENERGIZING("easyEnergizing", "Easy Energizing Breath", 4f, 2f, 2f, 0f),
    ENERGIZING("energizing", "Energizing Breath", 8f, 4f, 4f, 0f),
    BELLOWS("bellows", "Bellows Breath", 2f, 0f, 2f, 0f),
    CUSTOM("custom", "Custom Pranayama", null, null, null, null);

    companion object {
        fun fromId(id: String): BreathPreset =
            entries.find { it.id == id } ?: RELAXING
    }
}

enum class TransitionCue(val id: String, val displayName: String) {
    VOICE("voice", "Voice"),
    CHIME("chime", "Chime"),
    NONE("none", "None");

    companion object {
        fun fromId(id: String): TransitionCue =
            entries.find { it.id == id } ?: VOICE
    }
}

data class BreathPhase(
    val name: String,
    val durationSeconds: Float
)

data class BreathUiState(
    val breathType: BreathPreset = BreathPreset.RELAXING,
    val inhaleSeconds: Float = 4f,
    val holdInhaleSeconds: Float = 7f,
    val exhaleSeconds: Float = 8f,
    val holdExhaleSeconds: Float = 0f,
    val cyclesText: String = "∞",
    val cueType: TransitionCue = TransitionCue.VOICE,
    val isRunning: Boolean = false,
    val currentPhaseName: String = "Inhale",
    val currentPhaseElapsedSeconds: Float = 0.0f,
    val progressPercent: Float = 0f,
    val currentCycle: Int = 1,
    val maxCycles: Int? = null
) {
    val totalCycleDuration: Float
        get() = inhaleSeconds + holdInhaleSeconds + exhaleSeconds + holdExhaleSeconds
}
