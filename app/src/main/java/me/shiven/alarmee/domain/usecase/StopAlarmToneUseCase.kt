package me.shiven.alarmee.domain.usecase

import me.shiven.alarmee.domain.dummy.AlarmSoundPlayer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopAlarmToneUseCase @Inject constructor(
    private val alarmSoundPlayer: AlarmSoundPlayer,
) {
    operator fun invoke() {
        alarmSoundPlayer.stopAlarm()
    }
}