package me.shiven.alarmee.ui.permission

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import me.shiven.alarmee.ui.permission.permissionscreen.PermissionScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PermissionApp(onNavigationClick: () -> Unit) {
    val viewModel: PermissionViewModel = hiltViewModel()
    PermissionScreen(
        viewModel = viewModel,
        onNavigationClick = onNavigationClick
    )
}