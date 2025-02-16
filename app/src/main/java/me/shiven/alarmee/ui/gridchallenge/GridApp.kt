package me.shiven.alarmee.ui.gridchallenge

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun GridApp(modifier: Modifier = Modifier, onCompletionCallBack: () -> Unit) {
    val gridViewModel: GridViewModel = hiltViewModel()
    GridScreen(
        viewModel = gridViewModel,
        onCompletionCallback = onCompletionCallBack
    )
}