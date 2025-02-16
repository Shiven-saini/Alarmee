package me.shiven.alarmee.domain.usecase

import android.util.Log
import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.data.repository.AlarmRepository
import me.shiven.alarmee.data.scheduler.AlarmSchedulerImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetAlarmChallengeUseCase @Inject constructor(
    val alarmRepository: AlarmRepository,
    val alarmScheduler: AlarmSchedulerImpl
) {
    suspend operator fun invoke(challengeStatus: Boolean, alarm: Alarm) {

        // Cancel the previous alarm
        if(alarm.isEnabled) {
            alarmScheduler.cancelAlarm(alarm)
        }
        Log.d("UseCase", "Cancelled the old alarm!")

        val newAlarm = alarm.copy(
            challengeStatus = challengeStatus
        )
        alarmRepository.updateAlarm(newAlarm)
        Log.d("UseCase", "Scheduled and updated the challenge status of alarm.")
        Log.d("Usecase", "$newAlarm")
        if(newAlarm.isEnabled) {
            alarmScheduler.scheduleAlarm(newAlarm)
        }
    }
}