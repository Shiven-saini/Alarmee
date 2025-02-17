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


        alarmScheduler.cancelAlarm(alarm)
        println("Usecase : Cancelled the old alarm : $alarm")

        val newAlarm = alarm.copy(
            challengeStatus = challengeStatus
        )
        alarmRepository.updateAlarm(newAlarm)
        println("Use case : Scheduled the latest alarm : $newAlarm")
        if(newAlarm.isEnabled) {
            alarmScheduler.scheduleAlarm(newAlarm)
        }
    }
}