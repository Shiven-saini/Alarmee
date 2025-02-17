package me.shiven.alarmee.ui.welcome

import androidx.compose.runtime.Composable
import me.shiven.alarmee.ui.welcome.welcomescreen.WelcomeScreen

@Composable
fun WelcomeApp(
    onProceedClick: () -> Unit
) {
    WelcomeScreen(
        onProceedClick = onProceedClick
    )
}
