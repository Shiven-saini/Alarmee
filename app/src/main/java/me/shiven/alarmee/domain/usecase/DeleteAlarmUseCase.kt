package me.shiven.alarmee.domain.usecase

import android.util.Log
import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.data.repository.AlarmRepository
import me.shiven.alarmee.data.scheduler.AlarmSchedulerImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmSchedulerImpl
) {
    suspend operator fun invoke(alarm: Alarm) {
        alarmRepository.deleteAlarm(alarm)
        Log.d("Usecase", "Just deleted an alarm hence cancelled!")
        alarmScheduler.cancelAlarm(alarm)
    }
}