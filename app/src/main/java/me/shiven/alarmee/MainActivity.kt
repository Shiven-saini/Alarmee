package me.shiven.alarmee

import me.shiven.alarmee.ui.alarm.alarmscreen.ChallengeSheet
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import dagger.hilt.android.AndroidEntryPoint
import me.shiven.alarmee.ui.alarm.AlarmApp
import me.shiven.alarmee.ui.theme.AlarmeeTheme

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlarmeeTheme {
                AlarmApp()
            }
        }
    }
}