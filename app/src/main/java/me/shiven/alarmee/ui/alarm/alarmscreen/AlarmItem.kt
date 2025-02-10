package me.shiven.alarmee.ui.alarm.alarmscreen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.ui.alarm.AlarmViewModel
import me.shiven.alarmee.ui.getToneNameFromUri
import me.shiven.alarmee.ui.parseRepeatDays

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
            Toast.makeText(context, "Not Implemented yet!", Toast.LENGTH_SHORT).show()
        },
        onMissionInfoClick = {
            Toast.makeText(context, "Not Implemented yet!", Toast.LENGTH_SHORT).show()
        },
        onDeleteClick = { viewModel.deleteAlarm(alarm) },
        selectedDays = parseRepeatDays(alarm.repeatDays),
        onDaySelected = { id -> viewModel.onDaySelectedChange(id, alarm) }
    )
}