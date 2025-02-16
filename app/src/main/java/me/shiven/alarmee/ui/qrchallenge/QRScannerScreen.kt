package me.shiven.alarmee.ui.qrchallenge

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import me.shiven.alarmee.ui.qrchallenge.components.QRScannerBox
import me.shiven.alarmee.ui.theme.AlarmeeTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QRScannerScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.CAMERA,
        onPermissionResult = { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Camera Permission Granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Camera Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(20.dp)
    ) {
        Text(
            text = "Challenge: QR Code",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.padding(bottom = 30.dp)
        )

        Text(
            text = "Scan the QR Code to complete the challenge",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = Modifier.padding(bottom = 120.dp)
        )

        QRScannerBox()

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun QRScannerScreenPreview() {
    AlarmeeTheme {
        QRScannerScreen()
    }
}

