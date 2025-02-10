package me.shiven.alarmee.domain.usecase

import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.data.repository.AlarmRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(hour: Int, minute: Int) {
        val isAm: Boolean = if (hour == 12) false else hour < 12
        val newAlarm = Alarm(
            hour = hour % 12,
            minute = minute,
            isAm = isAm
        )
        alarmRepository.addAlarm(newAlarm)
    }
}