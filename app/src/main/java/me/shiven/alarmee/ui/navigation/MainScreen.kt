package me.shiven.alarmee.ui.navigation


import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.shiven.alarmee.ui.about.AboutApp
import me.shiven.alarmee.ui.alarm.AlarmApp
import me.shiven.alarmee.ui.timer.TimerApp

// Data class for bottom navigation items
data class BottomNavItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Dummy state to force recomposition when the activity resumes.
    val permissionUpdateTrigger = remember { mutableStateOf(0) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // Increment the trigger state value so that recomposition occurs
                permissionUpdateTrigger.value++
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    // Read the trigger to ensure recomposition.
    val dummy = permissionUpdateTrigger.value

    // Check notification permission.
    val hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }

    // Check full screen permission (only applicable for Android 14+).
    val hasFullScreenPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.canUseFullScreenIntent()
    } else {
        true
    }

    // The underlying UI of the MainScreen.
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "alarm",
                modifier = Modifier
                    .padding(bottom = 74.dp)
            ) {
                composable("alarm") { AlarmApp() }
                composable("timer") { TimerApp() }
                composable("about") { AboutApp() }
            }
        }

        // Show the notification permission dialog if not granted.
        if (!hasNotificationPermission) {
            AlertDialog(
                onDismissRequest = { /* Block dismissing the dialog via back press or clicking outside */ },
                title = { Text(text = "Notification Permission Required") },
                text = {
                    Text(
                        text = "This app requires notification permission for proper functionality." +
                                "Please enable it in Settings."
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // Open the app notification settings page.
                            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                            }
                            context.startActivity(intent)
                        }
                    ) {
                        Text(text = "Allow")
                    }
                }
            )
        }
        // Only if notification permission is granted, check for full screen permission (on Android 14+).
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && !hasFullScreenPermission) {
            AlertDialog(
                onDismissRequest = { /* Block dismissing the dialog */ },
                title = { Text(text = "Full Screen Notification Permission Required") },
                text = {
                    Text(
                        text = "On Android 14+ devices, the app requires full screen notification permission to function properly." +
                                "Please enable it in Settings."
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // Open the full screen intent settings page.
                            val intent = Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT).apply {
                                data = Uri.parse("package:${context.packageName}")
                            }
                            context.startActivity(intent)
                        }
                    ) {
                        Text(text = "Allow")
                    }
                }
            )
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    // Define bottom navigation items.
    val items = listOf(
        BottomNavItem("alarm", "Alarm", Icons.Filled.Alarm),
        BottomNavItem("timer", "Timer", Icons.Filled.Timer),
        BottomNavItem("about", "About", Icons.Filled.Info)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(modifier = Modifier.clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))) {
        items.forEach { item ->
            NavigationBarItem(
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
