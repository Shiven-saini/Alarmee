package me.shiven.alarmee.ui.nfcchallenge

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.shiven.alarmee.ui.nfcchallenge.components.NFCBox

@Composable
fun NFCScreen(
    viewModel: NFCViewModel,
    onCompletionCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isNFCEnabled = viewModel.isNFCEnabled.collectAsState()

// Start scanning when the composable launches.
    LaunchedEffect(Unit) {
        viewModel.startScanningForNfcTag()
    }

// Collect toast messages and show a toast.
    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

// Listen for the success event and call the onCompletionCallback.
    LaunchedEffect(Unit) {
        viewModel.nfcSuccessEvent.collect {
            onCompletionCallback()
        }
    }

// Wrap the content in a Surface for a modern Material 3 look.
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Challenge: NFC Tag",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Tap the NFC Tag to complete the challenge",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            NFCBox(
                isNFCEnabled = isNFCEnabled.value,
                onIconClick = viewModel::openNFCSettings
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
