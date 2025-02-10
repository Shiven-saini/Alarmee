package me.shiven.alarmee.ui.userinfo

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import me.shiven.alarmee.ui.userinfo.userscreen.UserScreen

@ExperimentalMaterial3Api
@Composable
fun UserInfoApp(
) {
    UserScreen(
        onNavigationClick = {}
    )
}