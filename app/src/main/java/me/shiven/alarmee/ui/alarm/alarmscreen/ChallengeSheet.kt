package me.shiven.alarmee.ui.alarm.alarmscreen

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.shiven.alarmee.DismissActivity
import me.shiven.alarmee.ui.alarm.AlarmViewModel


enum class CardSelection {
    GRID_GAME,
    QR_CODE,
    NFC_TAG
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeSheet(
    showSheet: Boolean,
    sheetState: SheetState,
    onCardClick: (CardSelection) -> Unit,
    cardActive: CardSelection,
    onDismissRequest: () -> Unit,
//    viewModel: AlarmViewModel
) {
    val context = LocalContext.current

    val intent = Intent(context, DismissActivity::class.java)
    intent.putExtra("toneUri", "content://settings/system/alarm_alert")

    Box(modifier = Modifier.navigationBarsPadding()) {
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = onDismissRequest,
                sheetState = sheetState,
                dragHandle = { BottomSheetDefaults.DragHandle() },
                shape = MaterialTheme.shapes.large,
                containerColor = MaterialTheme.colorScheme.surface,
                content = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        ChallengeCard(
                            onCardClick = {

                            },
                            onDemoClick = {
                                intent.putExtra("startDestinationID", "gridGame")
                                context.startActivity(intent)
                            },
                            imageVector = Icons.Filled.GridOn,
                            headerText = "Grid Game"
                        )
                        ChallengeCard(
                            onCardClick = {  },
                            onDemoClick = {
                                intent.putExtra("startDestinationID", "qrCode")
                                context.startActivity(intent)
                            },
                            imageVector = Icons.Filled.QrCode,
                            headerText = "Scan QR Code"
                        )
                        ChallengeCard(
                            onCardClick = {  },
                            onDemoClick = {
                                intent.putExtra("startDestinationID", "nfcTag")
                                context.startActivity(intent)
                            },
                            imageVector = Icons.Filled.Nfc,
                            headerText = "Use NFC Tag"
                        )
                    }
                }
            )
        }
    }
}

