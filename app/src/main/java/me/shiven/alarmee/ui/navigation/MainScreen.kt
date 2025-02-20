package me.shiven.alarmee.ui.navigation


import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.core.app.NotificationManagerCompat
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

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current


    var isFullScreenPermissionGranted by remember { mutableStateOf(true) }
    // Notification permission state
    val notificationPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )

    // State to track if full-screen notification permission is granted
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        isFullScreenPermissionGranted = false
    }

    // This flag is set after notification permission is granted and delay elapsed.
    var delayForFullScreenCheck by remember { mutableStateOf(false) }

    // Request notification permission immediately.
    LaunchedEffect(notificationPermissionState.status) {
        if (notificationPermissionState.status !is PermissionStatus.Granted) {
            notificationPermissionState.launchPermissionRequest()
        } else {
            // Once notification permission is granted, wait a bit before checking full-screen permission.
            delay(300L)
            delayForFullScreenCheck = true
        }
    }

    // Continuously poll for full-screen permission until it is granted.
    LaunchedEffect(delayForFullScreenCheck) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            if (delayForFullScreenCheck) {
                while (!isFullScreenNotificationPermissionGranted(context)) {
                    // Wait for one second before checking again.
                    delay(1000L)
                }
                // Once the permission is granted, update the state.
                isFullScreenPermissionGranted = true
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main UI Content
        Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) { innerPadding ->
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

        // Block UI with Notification permission dialog if not granted.
        if (notificationPermissionState.status !is PermissionStatus.Granted) {
            Dialog(onDismissRequest = { /* Prevent dismissal until permission is granted */ }) {
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
                            text = "Notification Permission Required",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "This app requires notification permission for full functionality. Please tap the button below to open app settings.",
                            style = MaterialTheme.typography.bodyLarge,
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
        // Otherwise, if full-screen permission is not granted, block the UI with its dialog.
        else if (!isFullScreenPermissionGranted) {
            Dialog(onDismissRequest = { /* Block dismissal until permission is granted */ }) {
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
                            text = "Special App Notification Permission Required",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "This app requires special full-screen notification access. Please enable it in the settings.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { openFullScreenIntentSettings(context) },
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Text(
                                text = "Open App Settings",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}


fun isFullScreenNotificationPermissionGranted(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            "android:use_full_screen_intent",
            android.os.Process.myUid(),
            context.packageName
        )
        mode == AppOpsManager.MODE_ALLOWED
    } else {
        true
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun openFullScreenIntentSettings(context: Context) {
    val intent = Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT)
        .setData(Uri.parse("package:${context.packageName}"))
    context.startActivity(intent)
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
