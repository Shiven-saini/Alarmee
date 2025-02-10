package me.shiven.alarmee.domain.usecase

import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.data.repository.AlarmRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnableAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(enableStatus: Boolean, alarm: Alarm) {
        val updatedAlarm = alarm.copy(isEnabled = enableStatus)
        alarmRepository.updateAlarm(updatedAlarm)
    }
}