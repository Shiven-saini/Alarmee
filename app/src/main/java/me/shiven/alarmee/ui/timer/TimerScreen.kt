package me.shiven.alarmee.ui.timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import me.shiven.alarmee.data.OrientationListener
import me.shiven.alarmee.ui.getSystemAnimationScale
import me.shiven.alarmee.ui.timeFormatHelper

@Composable
fun TimerScreen(
    isFirstLaunch: Boolean,
    viewModel: TimerViewModel,
    modifier: Modifier = Modifier
) {
    val toShowDialog = remember { mutableStateOf(false) }
    val toShowPicker = remember { mutableStateOf(false) }
    val toShowHelpIcon = remember { mutableStateOf(true) } // Show help icon initially
    val progress = remember { Animatable(0f) }
    val uiState = viewModel.uiState.collectAsState()
    val currentTime = remember { mutableStateOf(0) }
    val animationScale = remember { mutableStateOf(1f) }
    val context = LocalContext.current
    var isInverted by remember { mutableStateOf(false) }
    val justLaunched = remember { mutableStateOf(true) }

    val orientationListener = remember {
        OrientationListener(context) { orientation ->
            isInverted = orientation
        }
    }

    DisposableEffect(Unit) {
        orientationListener.startListening()
        onDispose {
            orientationListener.stopListening()
        }
    }

    // Reset and start timer when inverted (except for first launch)
    LaunchedEffect(key1 = isInverted) {
        if (!justLaunched.value) {
            viewModel.resetAndStartTimer()
        } else {
            justLaunched.value = false
        }
    }

    // Get system animation scale
    LaunchedEffect(Unit) {
        animationScale.value = getSystemAnimationScale(context)
    }

    // Handle progress animation when timer is enabled
    LaunchedEffect(key1 = uiState.value.isTimerEnabled) {
        if (uiState.value.isTimerEnabled) {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = (uiState.value.timerDuration * 1000 / animationScale.value).toInt(),
                    easing = LinearEasing
                )
            )
        } else {
            progress.snapTo(targetValue = 0f)
        }
    }

    // Countdown logic for timer duration
    LaunchedEffect(key1 = uiState.value.timerDuration, key2 = uiState.value.isTimerEnabled) {
        currentTime.value = uiState.value.timerDuration
        if (uiState.value.isTimerEnabled) {
            while (currentTime.value > 0) {
                delay(1000)
                currentTime.value--
            }
        }
    }

    // Hide help icon and dismiss dialog when timer starts
    LaunchedEffect(key1 = uiState.value.isTimerEnabled) {
        if (uiState.value.isTimerEnabled) {
            toShowHelpIcon.value = false // Hide help icon
            toShowDialog.value = false  // Dismiss LaunchDialog if open
        }
    }

    AnimatedVisibility(
        visible = toShowDialog.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LaunchDialog {
            toShowDialog.value = false // Dismiss dialog on user action
        }
    }

    AnimatedVisibility(
        visible = toShowPicker.value,
        enter = fadeIn(),
        exit = fadeOut()
    ){
        DurationPickerDialog(
            isInverted = isInverted,
            onDismiss = { toShowPicker.value = false },
            onConfirm = { minutes, seconds ->
                viewModel.setTimer(minutes, seconds)
                toShowPicker.value = false
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .graphicsLayer {
                rotationZ = if (isInverted) 180f else 0f
            }
            .clickable {
                toShowPicker.value = true
            }
    ) {
        if (toShowHelpIcon.value) {
            IconButton(
                onClick = { toShowDialog.value = true },
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                .padding(top = 40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Help, // Use a relevant icon
                    contentDescription = "Help",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(progress.value)
                .align(Alignment.BottomCenter)
                .background(Color.Gray)
        )

        Text(
            text = timeFormatHelper(currentTime.value),
            color = Color(0xFFFFD700),
            fontSize = 100.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
