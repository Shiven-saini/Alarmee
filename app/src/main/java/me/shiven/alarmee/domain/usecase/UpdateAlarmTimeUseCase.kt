package me.shiven.alarmee.domain.usecase

import android.util.Log
import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.data.repository.AlarmRepository
import me.shiven.alarmee.data.scheduler.AlarmSchedulerImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateAlarmTimeUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmSchedulerImpl
){
    suspend operator fun invoke(hour: Int, minute: Int, alarm: Alarm){
        // Cancel the already existing alarm
        alarmScheduler.cancelAlarm(alarm)

        val isAm: Boolean = if (hour == 12) false else hour < 12

        // updating the alarm with new time.
        val newAlarm = alarm.copy(
            hour = hour % 12,
            minute = minute,
            isAm = isAm
        )

        Log.d("Usecase", "Updated the time in our alarm!")
        alarmRepository.updateAlarm(newAlarm)

        Log.d("Usecase", "Just checked whether to schedule the alarm or not!")
        if(newAlarm.isEnabled) {
            alarmScheduler.scheduleAlarm(newAlarm)
        }
    }
}