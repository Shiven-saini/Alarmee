package me.shiven.alarmee.ui.alarm.alarmscreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("DefaultLocale")
@Composable
fun AlarmWidget(
    hour: Int,
    minute: Int,
    meridian: String = "am",
    isAlarmEnabled: Boolean = false,
    onAlarmEnable: (Boolean) -> Unit = {},
    isChallengeEnabled: Boolean = false,
    onChallengeEnable: (Boolean) -> Unit,
    selectedToneName: String = "Melody-chime",
    onTonePickerClick: () -> Unit = {},
    onChallengeClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    selectedDays: Set<Int> = setOf(1),
    onDaySelected: (Int) -> Unit = {},
    onTimeClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {

    // Internal state and I think, there is no need to hoist this state.
    var isExpanded by remember { mutableStateOf(false) }
    val timeDialogState = rememberUseCaseState()

    ClockDialog(
        state = timeDialogState,
        config = ClockConfig(
            is24HourFormat = false
        ),
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            Log.d("Usecase", "Just updated the time of the alarm")
            onTimeClick(hours, minutes)
        }
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        ),
        modifier = modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(1.dp, MaterialTheme.colorScheme.outline, shape = CircleShape)
                    .align(Alignment.End)
                    .clickable { isExpanded = !isExpanded }
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable { timeDialogState.show() }
            ) {
                Text(
                    text = String.format("%d:%02d", hour, minute),
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Text(
                    text = meridian,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isAlarmEnabled) "Scheduled" else "Not Scheduled",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                SwitchWidget(
                    checked = isAlarmEnabled,
                    onCheckedChange = onAlarmEnable
                )
            }

            if (isExpanded) {

                // WeekDays selection widget
                WeekdaySelection(
                    selectedDays = selectedDays,
                    onDaySelected = onDaySelected
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = onTonePickerClick
                        )
                        .padding(start = 10.dp, bottom = 12.dp, top = 14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.NotificationsActive,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = selectedToneName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onChallengeClick)
                        .padding(start = 10.dp, bottom = 12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Verified,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        )
                        Text(
                            text = "Wake-up Challenge",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }

                    SwitchWidget(
                        checked = isChallengeEnabled,
                        onCheckedChange = onChallengeEnable
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = onDeleteClick
                        )
                        .padding(start = 10.dp, bottom = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                    Text(
                        text = "Delete",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}


@Composable
fun SwitchWidget(checked: Boolean = false, onCheckedChange: (Boolean) -> Unit = {}) {
    Switch(
        colors = SwitchDefaults.colors(
            // TODO: Implement color scheme
        ),
        checked = checked,
        onCheckedChange = { it ->
            onCheckedChange(it)
        },
        thumbContent = {
            if (checked) {
                Icon(
                    imageVector = Icons.Rounded.Done,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            }
        }
    )
}


//@Preview(
//    showBackground = true, showSystemUi = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
//)
//@Composable
//private fun AlarmWidgetPreview() {
//    AlarmeeTheme{
//        Column(
//            Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            AlarmWidget(
//                hour = 5,
//                minute = 30,
//                onTimeClick = (Int, Int) -> Unit
//            )
//        }
//    }
//}