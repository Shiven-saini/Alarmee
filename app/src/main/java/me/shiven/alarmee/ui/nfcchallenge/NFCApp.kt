package me.shiven.alarmee.ui.nfcchallenge

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NFCApp(onCompletionCallback: () -> Unit) {
    val nfcViewModel: NFCViewModel = hiltViewModel()
    NFCScreen(nfcViewModel, onCompletionCallback)
}