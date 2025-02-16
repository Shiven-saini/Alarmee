package me.shiven.alarmee.ui.alarm.alarmscreen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.domain.model.ChallengeList
import me.shiven.alarmee.ui.alarm.AlarmViewModel
import me.shiven.alarmee.ui.getToneNameFromUri
import me.shiven.alarmee.ui.parseRepeatDays

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmItem(
    alarm: Alarm,
    context: Context,
    viewModel: AlarmViewModel,
    onTonePickerClick: () -> Unit
) {

    AlarmWidget(
        hour = alarm.hour,
        minute = alarm.minute,
        meridian = if (alarm.isAm) "AM" else "PM",
        isAlarmEnabled = alarm.isEnabled,
        onAlarmEnable = { enableStatus -> viewModel.onAlarmEnable(enableStatus, alarm) },
        selectedToneName = getToneNameFromUri(Uri.parse(alarm.tone), context),
        onTonePickerClick = onTonePickerClick,
        onChallengeClick = {
        },
        isChallengeEnabled = alarm.challengeStatus,
        onChallengeEnable = {enableStatus -> viewModel.setAlarmChallenge(enableStatus, alarm)},
        onDeleteClick = { viewModel.deleteAlarm(alarm) },
        selectedDays = parseRepeatDays(alarm.repeatDays),
        onDaySelected = { id -> viewModel.onDaySelectedChange(id, alarm) },
        onTimeClick = { hours, minutes -> viewModel.updateAlarmTime(hours, minutes, alarm) }
    )
}