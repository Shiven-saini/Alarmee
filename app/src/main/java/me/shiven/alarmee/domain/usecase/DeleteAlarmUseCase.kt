package me.shiven.alarmee.domain.usecase

import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.data.repository.AlarmRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(alarm: Alarm) {
        alarmRepository.deleteAlarm(alarm)
    }
}