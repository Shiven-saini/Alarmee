package me.shiven.alarmee.ui.qrchallenge

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun QRApp(
    modifier: Modifier = Modifier,
    onDismissClick: () -> Unit
) {
    QRScannerScreen(
        onDismissClick = onDismissClick
    )
}
