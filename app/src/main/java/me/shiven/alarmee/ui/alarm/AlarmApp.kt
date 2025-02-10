package me.shiven.alarmee.ui.alarm

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import me.shiven.alarmee.ui.alarm.alarmscreen.AlarmScreen


// TODO: Add perfmanager to show that the onboarding is completed.

@Composable
fun AlarmApp() {
    val viewModel = hiltViewModel<AlarmViewModel>()

    AlarmScreen(viewModel = viewModel)
}