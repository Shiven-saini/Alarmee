package me.shiven.alarmee.ui.permission

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import me.shiven.alarmee.ui.permission.permissionscreen.PermissionScreen

@Composable
fun PermissionApp() {
    val viewModel: PermissionViewModel = hiltViewModel()
    PermissionScreen(
        viewModel = viewModel,
        onNavigationClick = {}
    )
}