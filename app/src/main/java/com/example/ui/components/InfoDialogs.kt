package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.BreathPreset
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGoldDark
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CardBg
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

@Composable
fun PranayamaMenuDialog(
    onDismiss: () -> Unit,
    onSelectPreset: (BreathPreset) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBg),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("menu_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SelfImprovement,
                            contentDescription = null,
                            tint = AccentGoldDark,
                            modifier = Modifier.size(26.dp)
                        )
                        Text(
                            text = "Aswin Breath Guide",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = "Select a practice below to load its traditional timings, or use the custom controls on the timer.",
                            fontSize = 13.sp,
                            color = TextMuted,
                            lineHeight = 18.sp
                        )
                    }

                    item {
                        GuideItem(
                            title = "Relaxing Breath (4-7-8)",
                            description = "Inhale 4s, Hold 7s, Exhale 8s. Dr. Andrew Weil's natural tranquilizer for the nervous system, ideal before sleep or when feeling stressed.",
                            onClick = {
                                onSelectPreset(BreathPreset.RELAXING)
                                onDismiss()
                            }
                        )
                    }

                    item {
                        GuideItem(
                            title = "Box Breath (4-4-4-4)",
                            description = "Equal inhale, hold, exhale, hold. Sama Vritti Pranayama creates instant mental clarity, emotional balance, and athletic focus.",
                            onClick = {
                                onSelectPreset(BreathPreset.BOX)
                                onDismiss()
                            }
                        )
                    }

                    item {
                        GuideItem(
                            title = "Easy Relaxing Breath (4-0-8)",
                            description = "Gentle 1:2 ratio breath without breath retention. Great for beginners easing into slow diaphragmatic breathing.",
                            onClick = {
                                onSelectPreset(BreathPreset.EASY_RELAXING)
                                onDismiss()
                            }
                        )
                    }

                    item {
                        GuideItem(
                            title = "Easy Energizing Breath (4-2-2)",
                            description = "Smooth inhale with short retention. Invigorates the body and clears mental fog during the afternoon.",
                            onClick = {
                                onSelectPreset(BreathPreset.EASY_ENERGIZING)
                                onDismiss()
                            }
                        )
                    }

                    item {
                        GuideItem(
                            title = "Energizing Breath (8-4-4)",
                            description = "Deep prolonged intake that oxygenates every cell and awakens natural alertness.",
                            onClick = {
                                onSelectPreset(BreathPreset.ENERGIZING)
                                onDismiss()
                            }
                        )
                    }

                    item {
                        GuideItem(
                            title = "Bellows Breath (2-0-2)",
                            description = "Bhastrika Pranayama: rapid rhythmic breathing to stimulate digestive fire and clear the respiratory pathways.",
                            onClick = {
                                onSelectPreset(BreathPreset.BELLOWS)
                                onDismiss()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GuideItem(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AccentGoldDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = TextMain,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun SearchPresetsDialog(
    onDismiss: () -> Unit,
    onSelectPreset: (BreathPreset) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(query) {
        if (query.isBlank()) {
            BreathPreset.entries
        } else {
            BreathPreset.entries.filter {
                it.displayName.contains(query, ignoreCase = true)
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBg),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("search_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Search Breathing Patterns",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )

                    IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search e.g. Box, Relaxing, 4-7-8", color = TextMuted, fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_input"),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filtered) { preset ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectPreset(preset)
                                    onDismiss()
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = preset.displayName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextMain
                                )
                                if (preset.inhale != null) {
                                    Text(
                                        text = "${preset.inhale.toInt()}-${preset.holdInhale?.toInt() ?: 0}-${preset.exhale?.toInt() ?: 0}-${preset.holdExhale?.toInt() ?: 0}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentGoldDark
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ZenGearDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null,
                    tint = AccentGoldDark
                )
                Text("Asivana Meditation & Breath", fontWeight = FontWeight.Bold, color = TextMain)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Aswin Breath is designed with reverence to classical Pranayama traditions.",
                    fontSize = 13.5.sp,
                    color = TextMain
                )
                Text(
                    text = "• Posture Tip: Sit comfortably with an upright spine and relaxed shoulders.\n• Breath Tip: Breathe gently through the nose into the abdomen rather than the upper chest.\n• Sound Cues: Keep your eyes closed and follow the soothing harmonic Solfeggio chime or gentle voice cues.",
                    fontSize = 12.5.sp,
                    color = TextMuted,
                    lineHeight = 18.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Namaste", color = AccentGoldDark, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = CardBg,
        shape = RoundedCornerShape(20.dp)
    )
}
