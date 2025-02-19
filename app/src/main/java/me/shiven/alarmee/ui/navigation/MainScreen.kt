package me.shiven.alarmee.ui.navigation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay
import me.shiven.alarmee.ui.about.AboutApp
import me.shiven.alarmee.ui.alarm.AlarmApp
import me.shiven.alarmee.ui.timer.TimerApp

// Data class for bottom navigation items
data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {
// Create the navigation controller (existing code)
    val navController = rememberNavController()
    val context = LocalContext.current

// Create individual permission states using Accompanist Permissions.
// REQUIRED PERMISSIONS:
// Note: Replace the permission string below with the one appropriate for full screen notifications in your app.
    val notificationPermissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val fullScreenNotificationPermissionState =
        rememberPermissionState(permission = "android.permission.USE_FULL_SCREEN_INTENT")

// A state that allows the optional camera permission dialog to be dismissed by the user.
    var showCameraDialog by remember { mutableStateOf(true) }

// Immediately request the permissions on composition.
    LaunchedEffect(key1 = notificationPermissionState.status) {
// Request Notification Permission first
        if (notificationPermissionState.status !is PermissionStatus.Granted) {
            notificationPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(key1 = fullScreenNotificationPermissionState.status) {
// Only request Full Screen Notification Intent after Notification is granted.
        if (notificationPermissionState.status is PermissionStatus.Granted &&
            fullScreenNotificationPermissionState.status !is PermissionStatus.Granted
        ) {
            fullScreenNotificationPermissionState.launchPermissionRequest()
        }
    }


// Determine if any required permission is missing.
    val requiredPermissionsMissing =
        notificationPermissionState.status !is PermissionStatus.Granted ||
                fullScreenNotificationPermissionState.status !is PermissionStatus.Granted

// Wrap the main UI in a Box so that modal overlays can appear on top.
    Box(modifier = Modifier.fillMaxSize()) {
        // Existing UI: Scaffold with bottom navigation bar and NavHost
        Scaffold(
            bottomBar = { BottomNavigationBar(navController = navController) }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "alarm",
                modifier = Modifier.padding(bottom = 74.dp)
            ) {
                composable("alarm") { AlarmApp() }
                composable("timer") { TimerApp() }
                composable("about") { AboutApp() }
            }
        }

        // If any mandatory (required) permissions are missing, show a constant, non-dismissable dialog.
        if (requiredPermissionsMissing) {
            Dialog(
                onDismissRequest = { /* Intentionally block dismissal until settings are changed */ }
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 6.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Essential Permissions Required",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "This app requires Notification and Full Screen Notification permission for full functionality. " +
                                    "Please tap the button below to open the app settings.",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { openAppSettings(context) },
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Text(text = "Open App Settings")
                        }
                    }
                }
            }
        }
    }
}


// Bottom navigation bar composable that renders navigation items
@Composable
fun BottomNavigationBar(navController: NavController) {
    // Define the list of bottom navigation items
    val items = listOf(
        BottomNavItem("alarm", "Alarm", Icons.Filled.Alarm),
        BottomNavItem("timer", "Timer", Icons.Filled.Timer),
        BottomNavItem("about", "About", Icons.Filled.Info)
    )

    // Listen for navigation changes to update selected state
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = Modifier.clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
    ) { // Replace BottomNavigation with NavigationBar for Material 3
        items.forEach { item ->
            NavigationBarItem( // Replace BottomNavigationItem with NavigationBarItem
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

// Helper function to open the app's settings page.
fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}
