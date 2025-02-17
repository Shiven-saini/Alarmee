package me.shiven.alarmee.ui.alarm.alarmscreen

import android.content.Intent
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import me.shiven.alarmee.DismissActivity
import me.shiven.alarmee.domain.model.ChallengeList

@Composable
fun ChallengeDialog(selectedCard: ChallengeList, onDismissRequest: () -> Unit, onClickRequest: (selectedCardIndex: Int) -> Unit) {
    // State to track the selected card
    var selectedCard = when(selectedCard) {
        ChallengeList.GRID_GAME -> 0
        ChallengeList.NFC_TAG -> 1
        ChallengeList.QR_CODE -> 2
    }

    var selectedCardIndex by remember { mutableStateOf(selectedCard) }
    val context = LocalContext.current

    val intent = Intent(context, DismissActivity::class.java)
    intent.putExtra("toneUri", "content://settings/system/alarm_alert")

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Set Default Challenge", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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
                SelectableCard(
                    index = 1,
                    isSelected = selectedCardIndex == 1,
                    onClick = { selectedCardIndex = 1 },
                    imageVector = Icons.Rounded.Nfc,
                    challengeHeader = "Tap NFC Tag",
                    onDemoClick = {
                        intent.putExtra("startDestinationID", "nfcTag")
                        context.startActivity(intent)
                    }
                )

                SelectableCard(
                    index = 2,
                    isSelected = selectedCardIndex == 2,
                    onClick = { selectedCardIndex = 2 },
                    imageVector = Icons.Rounded.QrCode,
                    challengeHeader = "Scan QR Code",
                    onDemoClick = {
                        intent.putExtra("startDestinationID", "qrCode")
                        context.startActivity(intent)
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onClickRequest(selectedCardIndex) }) {
                Text(text = "Confirm")
            }
        }
    )
}

@Composable
fun SelectableCard(index: Int, isSelected: Boolean,
                   imageVector: ImageVector,
                   challengeHeader: String,
                   onClick: () -> Unit,
                   onDemoClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
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
                    modifier = Modifier.fillMaxSize()
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

                        ElevatedButton(onClick = onDemoClick) {
                            Text(text = "Demo")
                        }
                    }
                }
            }
        }
    }
}
