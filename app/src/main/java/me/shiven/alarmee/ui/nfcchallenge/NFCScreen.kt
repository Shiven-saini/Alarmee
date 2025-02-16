package me.shiven.alarmee.ui.nfcchallenge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import me.shiven.alarmee.ui.nfcchallenge.components.NFCBox

@Composable
fun NFCScreen(viewModel: NFCViewModel, modifier: Modifier = Modifier) {

    val isNFCEnabled = viewModel.isNFCEnabled.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(20.dp)
    ) {
        Text(
            text = "Challenge: NFC Tag",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.padding(bottom = 30.dp)
        )

        Text(
            text = "Tap the NFC Tag to complete the challenge",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        NFCBox(
            isNFCEnabled = isNFCEnabled.value,
            onIconClick = viewModel::openNFCSettings
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

    }
}

//@Preview
//@Composable
//private fun NFCScreenPreview() {
//    NFCScreen()
//}