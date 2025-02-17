package me.shiven.alarmee.ui.timer


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TimerApp(isFirstLaunch: Boolean = true , modifier: Modifier = Modifier) {

    val viewModel: TimerViewModel = TimerViewModel()
    TimerScreen(isFirstLaunch, viewModel)

}