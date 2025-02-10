package me.shiven.alarmee.ui.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.data.repository.AlarmRepository
import me.shiven.alarmee.domain.usecase.AddAlarmUseCase
import me.shiven.alarmee.domain.usecase.DeleteAlarmUseCase
import me.shiven.alarmee.domain.usecase.EnableAlarmUseCase
import me.shiven.alarmee.domain.usecase.UpdateAlarmDayUseCase
import me.shiven.alarmee.domain.usecase.UpdateAlarmToneUseCase
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val enableAlarmUseCase: EnableAlarmUseCase,
    private val addAlarmUseCase: AddAlarmUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val updateAlarmToneUseCase: UpdateAlarmToneUseCase,
    private val updateAlarmDayUseCase: UpdateAlarmDayUseCase,
    private val alarmRepository: AlarmRepository
): ViewModel() {

    val alarms: StateFlow<List<Alarm>> = alarmRepository.getAllAlarms()
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onAlarmEnable(enableStatus: Boolean, alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        enableAlarmUseCase(enableStatus, alarm)
    }

    fun onDaySelectedChange(id: Int, alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        updateAlarmDayUseCase(id, alarm)
    }

    fun onTonePickChange(alarmUri: String, alarm: Alarm?) = viewModelScope.launch(Dispatchers.IO) {
        updateAlarmToneUseCase(alarmUri, alarm)
    }

    fun addAlarm(hour: Int, minute: Int) = viewModelScope.launch(Dispatchers.IO) {
        addAlarmUseCase(hour, minute)
    }

    fun deleteAlarm(alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        deleteAlarmUseCase(alarm)
    }
}

val daysMap = mapOf(
    0 to "Sun",
    1 to "Mon",
    2 to "Tue",
    3 to "Wed",
    4 to "Thu",
    5 to "Fri",
    6 to "Sat"
)