package me.shiven.alarmee.ui.alarm.alarmscreen

import android.content.Intent
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Nfc
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import me.shiven.alarmee.DismissActivity
import me.shiven.alarmee.domain.model.ChallengeList

@Composable
fun ChallengeDialog(
    selectedCard: ChallengeList,
    onDismissRequest: () -> Unit,
    isQrDatabaseEmpty: Boolean,
    onClickRequest: (selectedCardIndex: Int) -> Unit
) {
    // Determine the initial selected card index based on the provided type
    val initialSelected = when (selectedCard) {
        ChallengeList.GRID_GAME -> 0
        ChallengeList.NFC_TAG -> 1
        ChallengeList.QR_CODE -> 2
    }
    var selectedCardIndex by remember { mutableStateOf(initialSelected) }
    val context = LocalContext.current

    // Check NFC support and enabled state at the time the dialog is shown.
    val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
    val isNfcSupported = nfcAdapter != null
    val isNfcEnabled = isNfcSupported && nfcAdapter.isEnabled

    // Check for camera permission (for QR Code card).
    val isCameraPermissionGranted = remember {
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Store NFC states in remembered state variables.
    val nfcSupportedState = remember { mutableStateOf(isNfcSupported) }
    val nfcEnabledState = remember { mutableStateOf(isNfcEnabled) }

    // Compute whether the NFC card should be considered enabled.
    val nfcCardEnabled = nfcSupportedState.value && nfcEnabledState.value

    // Intent configuration for launching the demo.
    val intent = Intent(context, DismissActivity::class.java).apply {
        putExtra("toneUri", "content://settings/system/alarm_alert")
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Set Default Challenge", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Grid Game card (index 0) remains unchanged.
                SelectableCard(
                    index = 0,
                    isSelected = selectedCardIndex == 0,
                    onClick = { selectedCardIndex = 0 },
                    imageVector = Icons.Rounded.GridView,
                    challengeHeader = "Grid Game",
                    onDemoClick = {
                        intent.putExtra("startDestinationID", "gridGame")
                        context.startActivity(intent)
                    }
                )
                // NFC card (index 1) with added logic for checking support and enabled state.
                SelectableCard(
                    index = 1,
                    isSelected = selectedCardIndex == 1,
                    enabled = nfcCardEnabled,
                    onClick = {
                        if (nfcCardEnabled) {
                            selectedCardIndex = 1
                        } else {
                            if (nfcSupportedState.value) {
                                Toast.makeText(
                                    context,
                                    "NFC is disabled right now",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "NFC is not supported on this device",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    imageVector = Icons.Rounded.Nfc,
                    challengeHeader = "Tap NFC Tag",
                    onDemoClick = {
                        if (nfcCardEnabled) {
                            intent.putExtra("startDestinationID", "nfcTag")
                            context.startActivity(intent)
                        } else {
                            if (nfcSupportedState.value) {
                                Toast.makeText(
                                    context,
                                    "NFC is disabled right now",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "NFC is not supported on this device",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                )
                // QR Code card with camera permission check.
                SelectableCard(
                    index = 2,
                    isSelected = selectedCardIndex == 2,
                    enabled = isCameraPermissionGranted && !isQrDatabaseEmpty,
                    onClick = {
                        if (isCameraPermissionGranted && !isQrDatabaseEmpty) {
                            selectedCardIndex = 2
                        } else {
                            Toast.makeText(
                                context,
                                "Camera permission is not granted or you haven't generated QR Code yet!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    imageVector = Icons.Rounded.QrCode,
                    challengeHeader = "Scan QR Code",
                    onDemoClick = {
                        if (isCameraPermissionGranted) {
                            intent.putExtra("startDestinationID", "qrCode")
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(
                                context,
                                "Camera permission is not granted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onClickRequest(selectedCardIndex) }) {
                Text(text = "Confirm", style = MaterialTheme.typography.bodyMedium)
            }
        }
    )
}

@Composable
fun SelectableCard(
    index: Int,
    isSelected: Boolean,
    imageVector: ImageVector,
    challengeHeader: String,
    onClick: () -> Unit,
    onDemoClick: () -> Unit,
    enabled: Boolean = true,
) {
    // The overall card alpha is modified based on the enabled parameter.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .alpha(if (enabled) 1f else 0.5f)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected)
                MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = onClick,
                    modifier = Modifier.align(Alignment.TopStart)
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = challengeHeader,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = challengeHeader,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // The Demo button also appears visually disabled when required.
                        ElevatedButton(
                            onClick = onDemoClick,
                            modifier = Modifier.alpha(if (enabled) 1f else 0.5f)
                        ) {
                            Text(text = "Demo", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
