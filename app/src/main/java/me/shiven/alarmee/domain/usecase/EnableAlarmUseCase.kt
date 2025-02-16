package me.shiven.alarmee.domain.usecase

import android.util.Log
import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.data.repository.AlarmRepository
import me.shiven.alarmee.data.scheduler.AlarmSchedulerImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnableAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmSchedulerImpl
) {
    suspend operator fun invoke(enableStatus: Boolean, alarm: Alarm) {
        val updatedAlarm = alarm.copy(isEnabled = enableStatus)
        alarmRepository.updateAlarm(updatedAlarm)
        if(enableStatus) {
            Log.d("Usecase", "Scheduled an alarm : $enableStatus")
            alarmScheduler.scheduleAlarm(updatedAlarm)
        } else {
            Log.d("Usecase", "Cancelled an alarm : $enableStatus")
            alarmScheduler.cancelAlarm(updatedAlarm)
        }
    }
}