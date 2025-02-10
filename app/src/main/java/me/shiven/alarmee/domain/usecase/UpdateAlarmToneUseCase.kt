package me.shiven.alarmee.domain.usecase

import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.data.repository.AlarmRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateAlarmToneUseCase @Inject constructor (
    private val alarmRepository: AlarmRepository
){
    suspend operator fun invoke(alarmUri: String, alarm: Alarm?) {
        alarm?.let {
            alarmRepository.updateAlarm(
                alarm.copy(
                    tone = alarmUri
                )
            )
        }
    }
}