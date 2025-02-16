package me.shiven.alarmee.ui.dismiss

// DismissScreenRoute.kt
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import me.shiven.alarmee.ui.dismiss.dismiss_screen.DismissScreen

@Composable
fun DismissApp(onDismissNavigateTo: () -> Unit) {
    val viewModel: DismissScreenViewModel = hiltViewModel()
    DismissScreen(
        greeting = viewModel.greeting,
        clockTime = viewModel.clockTime,
        dateText = viewModel.dateText,
        onDismiss = onDismissNavigateTo
    )
}
