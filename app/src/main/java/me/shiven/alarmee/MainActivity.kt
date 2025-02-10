package me.shiven.alarmee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import me.shiven.alarmee.ui.alarm.AlarmApp
import me.shiven.alarmee.ui.permission.PermissionApp
import me.shiven.alarmee.ui.theme.AlarmeeTheme
import me.shiven.alarmee.ui.userinfo.UserInfoApp
import me.shiven.alarmee.ui.welcome.WelcomeApp

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