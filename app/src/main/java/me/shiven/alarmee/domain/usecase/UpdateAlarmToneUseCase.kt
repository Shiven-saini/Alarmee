package me.shiven.alarmee.domain.usecase

import android.util.Log
import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.data.repository.AlarmRepository
import me.shiven.alarmee.data.scheduler.AlarmSchedulerImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateAlarmToneUseCase @Inject constructor (
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmSchedulerImpl
){
    suspend operator fun invoke(alarmUri: String, alarm: Alarm?) {
        lateinit var newAlarm: Alarm
        alarm?.let {
            alarmScheduler.cancelAlarm(it)
        }

        alarm?.let {
            newAlarm = alarm.copy(
                tone = alarmUri
            )
        }

        alarmRepository.updateAlarm(newAlarm)
        Log.d("Usecase", "Just updated the tone")
        alarmScheduler.scheduleAlarm(newAlarm)
    }
}