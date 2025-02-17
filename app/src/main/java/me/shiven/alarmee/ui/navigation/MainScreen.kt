package me.shiven.alarmee.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.shiven.alarmee.ui.about.AboutApp
import me.shiven.alarmee.ui.alarm.AlarmApp
import me.shiven.alarmee.ui.timer.TimerApp

// Data class for bottom navigation items
data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)

// MainScreen that sets up Scaffold with bottom navigation and navigation graph
@Composable
fun MainScreen() {
    val navController = rememberNavController() // Instantiate navigation controller [1]
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) } // Set bottom bar [3]
    ) { innerPadding ->
        // NavHost handles the composable destinations
        NavHost(
            navController = navController,
            startDestination = "alarm",  // Default destination is "Alarm" [1]
        ) {
            composable("alarm") { AlarmApp() }  // Points to AlarmApp composable [1]
            composable("timer") { TimerApp() }  // Points to TimerApp composable [1]
            composable("about") { AboutApp() }  // Points to AboutApp composable [1]
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
