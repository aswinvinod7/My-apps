package com.example

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.HeaderBar
import com.example.ui.components.PranayamaMenuDialog
import com.example.ui.components.SearchPresetsDialog
import com.example.ui.components.TimerCard
import com.example.ui.components.ZenGearDialog
import com.example.ui.theme.AswinBreathTheme
import com.example.ui.theme.BgColor
import com.example.viewmodel.BreathViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AswinBreathTheme {
                AswinBreathApp()
            }
        }
    }
}

@Composable
fun AswinBreathApp(
    viewModel: BreathViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? Activity

    // Prevent device screen from turning off during a breathing session
    DisposableEffect(uiState.isRunning) {
        if (uiState.isRunning) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    var showMenuDialog by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showGearDialog by remember { mutableStateOf(false) }

    val safeInsets = WindowInsets.safeDrawing.asPaddingValues()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BgColor
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgColor)
                .padding(
                    top = safeInsets.calculateTopPadding(),
                    bottom = safeInsets.calculateBottomPadding()
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 440.dp)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Navigation Bar
                HeaderBar(
                    onMenuClick = { showMenuDialog = true },
                    onSearchClick = { showSearchDialog = true },
                    onCartClick = { showGearDialog = true }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Main Timer Card
                TimerCard(
                    uiState = uiState,
                    onPresetSelected = { viewModel.selectPreset(it) },
                    onInhaleChanged = { viewModel.updateInhale(it) },
                    onHoldInhaleChanged = { viewModel.updateHoldInhale(it) },
                    onExhaleChanged = { viewModel.updateExhale(it) },
                    onHoldExhaleChanged = { viewModel.updateHoldExhale(it) },
                    onCyclesChanged = { viewModel.updateCycles(it) },
                    onCueTypeChanged = { viewModel.updateCueType(it) },
                    onToggleStartStop = { viewModel.toggleStartStop() }
                )

                // Bottom spacing matching HTML `padding: 0 16px 40px`
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // Dialogs
    if (showMenuDialog) {
        PranayamaMenuDialog(
            onDismiss = { showMenuDialog = false },
            onSelectPreset = { preset -> viewModel.selectPreset(preset) }
        )
    }

    if (showSearchDialog) {
        SearchPresetsDialog(
            onDismiss = { showSearchDialog = false },
            onSelectPreset = { preset -> viewModel.selectPreset(preset) }
        )
    }

    if (showGearDialog) {
        ZenGearDialog(
            onDismiss = { showGearDialog = false }
        )
    }
}
