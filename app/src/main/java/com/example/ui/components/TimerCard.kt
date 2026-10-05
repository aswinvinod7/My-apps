package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BreathPreset
import com.example.model.BreathUiState
import com.example.model.TransitionCue
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGoldDark
import com.example.ui.theme.AccentGoldLight
import com.example.ui.theme.BorderColor
import com.example.ui.theme.BtnStopBg
import com.example.ui.theme.CardBg
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TrackBg
import java.util.Locale

@Composable
fun TimerCard(
    uiState: BreathUiState,
    modifier: Modifier = Modifier,
    onPresetSelected: (BreathPreset) -> Unit,
    onInhaleChanged: (Float) -> Unit,
    onHoldInhaleChanged: (Float) -> Unit,
    onExhaleChanged: (Float) -> Unit,
    onHoldExhaleChanged: (Float) -> Unit,
    onCyclesChanged: (String) -> Unit,
    onCueTypeChanged: (TransitionCue) -> Unit,
    onToggleStartStop: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(28.dp), ambientColor = Color(0x10000000))
            .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(28.dp))
            .testTag("timer_card"),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card Title
            Text(
                text = "Pranayama Breath Timer",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("card_title")
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Card Subtitle
            Text(
                text = "Cultivate calm and focus through guided breathing",
                fontSize = 13.5.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 19.sp,
                modifier = Modifier.testTag("card_subtitle")
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Breath Type Dropdown
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "BREATH TYPE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 7.dp)
                )

                BreathTypeSelector(
                    selectedPreset = uiState.breathType,
                    onPresetSelected = onPresetSelected,
                    enabled = !uiState.isRunning
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Custom Timing Box (Bordered Fieldset)
            CustomTimingBox(
                uiState = uiState,
                onInhaleChanged = onInhaleChanged,
                onHoldInhaleChanged = onHoldInhaleChanged,
                onExhaleChanged = onExhaleChanged,
                onHoldExhaleChanged = onHoldExhaleChanged,
                onCyclesChanged = onCyclesChanged,
                onCueTypeChanged = onCueTypeChanged,
                enabled = !uiState.isRunning
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Duration Summary
            val formattedTotal = if (uiState.totalCycleDuration % 1.0f == 0.0f) {
                uiState.totalCycleDuration.toInt().toString()
            } else {
                String.format(Locale.US, "%.1f", uiState.totalCycleDuration)
            }
            Text(
                text = "Total Cycle Duration: $formattedTotal sec",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMain,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .testTag("total_duration")
            )

            // Start / Stop Action Button
            ActionButton(
                isRunning = uiState.isRunning,
                onClick = onToggleStartStop
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Active Visualization Area
            ActiveDisplayArea(uiState = uiState)

            Spacer(modifier = Modifier.height(24.dp))

            // Footer Disclaimer
            Text(
                text = "Practice pranayama techniques at your own risk. Consult your doctor before beginning any breathing practice.",
                fontSize = 11.sp,
                color = Color(0xFF8C8276),
                textAlign = TextAlign.Center,
                lineHeight = 15.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun BreathTypeSelector(
    selectedPreset: BreathPreset,
    onPresetSelected: (BreathPreset) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Color.White)
            .border(width = 1.5.dp, color = AccentGold, shape = RoundedCornerShape(25.dp))
            .clickable(enabled = enabled) { expanded = true }
            .padding(horizontal = 18.dp)
            .testTag("breath_type_selector"),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedPreset.displayName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = TextMain
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Select breath type",
                tint = Color(0xFF8E8376),
                modifier = Modifier.size(24.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
        ) {
            BreathPreset.entries.forEach { preset ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = preset.displayName,
                            fontWeight = if (preset == selectedPreset) FontWeight.Bold else FontWeight.Normal,
                            color = if (preset == selectedPreset) AccentGoldDark else TextMain
                        )
                    },
                    onClick = {
                        onPresetSelected(preset)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors()
                )
            }
        }
    }
}

@Composable
fun CustomTimingBox(
    uiState: BreathUiState,
    onInhaleChanged: (Float) -> Unit,
    onHoldInhaleChanged: (Float) -> Unit,
    onExhaleChanged: (Float) -> Unit,
    onHoldExhaleChanged: (Float) -> Unit,
    onCyclesChanged: (String) -> Unit,
    onCueTypeChanged: (TransitionCue) -> Unit,
    enabled: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 16.dp)
            .testTag("timing_box")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "CUSTOM TIMING (SECONDS)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.6.sp,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // 2-Column Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Inhale
                PillNumberInput(
                    label = "INHALE",
                    value = uiState.inhaleSeconds,
                    onValueChange = onInhaleChanged,
                    enabled = enabled,
                    modifier = Modifier.weight(1f),
                    tag = "input_inhale"
                )

                // Hold Inhale
                PillNumberInput(
                    label = "HOLD INHALE",
                    value = uiState.holdInhaleSeconds,
                    onValueChange = onHoldInhaleChanged,
                    enabled = enabled,
                    modifier = Modifier.weight(1f),
                    tag = "input_hold_inhale"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Exhale
                PillNumberInput(
                    label = "EXHALE",
                    value = uiState.exhaleSeconds,
                    onValueChange = onExhaleChanged,
                    enabled = enabled,
                    modifier = Modifier.weight(1f),
                    tag = "input_exhale"
                )

                // Hold Exhale
                PillNumberInput(
                    label = "HOLD EXHALE",
                    value = uiState.holdExhaleSeconds,
                    onValueChange = onHoldExhaleChanged,
                    enabled = enabled,
                    modifier = Modifier.weight(1f),
                    tag = "input_hold_exhale"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Cycles
                CyclesPillInput(
                    label = "CYCLES",
                    value = uiState.cyclesText,
                    onValueChange = onCyclesChanged,
                    enabled = enabled,
                    modifier = Modifier.weight(1f)
                )

                // Transition Cue Select
                CueTypeSelector(
                    label = "TRANSITION CUE",
                    selectedCue = uiState.cueType,
                    onCueSelected = onCueTypeChanged,
                    enabled = enabled,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun PillNumberInput(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    tag: String
) {
    val focusManager = LocalFocusManager.current
    val displayValue = if (value % 1.0f == 0.0f) value.toInt().toString() else String.format(Locale.US, "%.1f", value)
    var textState by remember(value) { mutableStateOf(displayValue) }

    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = textState,
                onValueChange = { newText ->
                    if (newText.all { it.isDigit() || it == '.' }) {
                        textState = newText
                        val parsed = newText.toFloatOrNull()
                        if (parsed != null && parsed >= 0f) {
                            onValueChange(parsed)
                        }
                    }
                },
                enabled = enabled,
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMain,
                    textAlign = TextAlign.Center
                ),
                cursorBrush = SolidColor(AccentGold),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(tag)
            )
        }
    }
}

@Composable
fun CyclesPillInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var textState by remember(value) { mutableStateOf(value) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                color = TextMuted
            )

            if (enabled && value != "∞") {
                Text(
                    text = "set ∞",
                    fontSize = 10.5.sp,
                    color = AccentGoldDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        textState = "∞"
                        onValueChange("∞")
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = textState,
                onValueChange = { newText ->
                    textState = newText
                    onValueChange(newText)
                },
                enabled = enabled,
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMain,
                    textAlign = TextAlign.Center
                ),
                cursorBrush = SolidColor(AccentGold),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_cycles")
            )
        }
    }
}

@Composable
fun CueTypeSelector(
    label: String,
    selectedCue: TransitionCue,
    onCueSelected: (TransitionCue) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(20.dp))
                .clickable(enabled = enabled) { expanded = true }
                .padding(start = 12.dp, end = 8.dp)
                .testTag("cue_type_selector"),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedCue.displayName,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMain,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select transition cue",
                    tint = Color(0xFF8E8376),
                    modifier = Modifier.size(20.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(Color.White)
                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            ) {
                TransitionCue.entries.forEach { cue ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = cue.displayName,
                                fontWeight = if (cue == selectedCue) FontWeight.Bold else FontWeight.Normal,
                                color = if (cue == selectedCue) AccentGoldDark else TextMain
                            )
                        },
                        onClick = {
                            onCueSelected(cue)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ActionButton(
    isRunning: Boolean,
    onClick: () -> Unit
) {
    if (isRunning) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("action_button_stop"),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BtnStopBg,
                contentColor = TextMain
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = "Stop",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(25.dp), ambientColor = AccentGoldDark)
                .clip(RoundedCornerShape(25.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(AccentGold, AccentGoldDark)
                    )
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
                .testTag("action_button_start"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Start",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun ActiveDisplayArea(
    uiState: BreathUiState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("display_area"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Phase label with height reservation
        Box(
            modifier = Modifier
                .height(28.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isRunning) {
                Text(
                    text = uiState.currentPhaseName,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("phase_label")
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Progress Track & Animated Fill
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(TrackBg)
                .testTag("progress_track"),
            contentAlignment = Alignment.CenterStart
        ) {
            val progressFraction = if (uiState.isRunning) {
                (uiState.progressPercent / 100f).coerceIn(0f, 1f)
            } else {
                0f
            }

            if (progressFraction > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = progressFraction)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFFD3911C), AccentGoldLight)
                            )
                        )
                        .testTag("progress_fill")
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Live Meta: Elapsed Time & Cycle Counter
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        ) {
            if (uiState.isRunning) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("live_meta"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val formattedElapsed = String.format(Locale.US, "%.1fs", uiState.currentPhaseElapsedSeconds)
                    Text(
                        text = formattedElapsed,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMain,
                        modifier = Modifier.testTag("time_elapsed")
                    )

                    val cycleString = if (uiState.maxCycles != null) {
                        "Cycle ${uiState.currentCycle} / ${uiState.maxCycles}"
                    } else {
                        "Cycle ${uiState.currentCycle}"
                    }

                    Text(
                        text = cycleString,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMain,
                        modifier = Modifier.testTag("cycle_counter")
                    )
                }
            }
        }
    }
}
