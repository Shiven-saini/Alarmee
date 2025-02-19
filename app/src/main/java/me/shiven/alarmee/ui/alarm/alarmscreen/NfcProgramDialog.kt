package me.shiven.alarmee.ui.alarm.alarmscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.shiven.alarmee.ui.alarm.NfcProgramState
import me.shiven.alarmee.ui.alarm.NfcViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NfcProgramDialog(
    viewModel: NfcViewModel = hiltViewModel(), // Obtain your view model via DI or viewModel()
    onDismiss: () -> Unit
) {
// Start the NFC detection process when the dialog appears.
    LaunchedEffect(Unit) {
        viewModel.startNfcProcess()
    }

// Observe the NFC programming state.
    val programState by viewModel.programState.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Filled.Nfc,
                contentDescription = "NFC Icon",
                modifier = Modifier.size(64.dp)
            )
        },
        title = {
            Text(text = "Program NFC Tag")
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Hold your NFC tag near the device to program it.",
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedContent(
                    targetState = programState,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(500)) with fadeOut(
                            animationSpec = tween(
                                500
                            )
                        )
                    }
                ) { state ->
                    when (state) {
                        NfcProgramState.Loading, NfcProgramState.Error -> {
                            CircularProgressIndicator(modifier = Modifier.size(64.dp))
                        }

                        NfcProgramState.Success -> {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Programmed Successfully",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
