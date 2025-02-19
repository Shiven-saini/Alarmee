package me.shiven.alarmee.domain.usecase

import android.net.Uri
import me.shiven.alarmee.domain.dummy.AlarmSoundPlayer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayAlarmToneUseCase @Inject constructor(
    private val alarmSoundPlayer: AlarmSoundPlayer,
) {
    // TODO: Add implementation and safe guards for default RingtoneUri  or use Ringtone manager to check.
    operator fun invoke(tone: String?) {
        val toneUri: Uri = if(tone != null) {
            Uri.parse(tone)
        } else {
            Uri.parse("content://settings/system/alarm_alert")
        }

        println("formatted tone uri : $toneUri")
        alarmSoundPlayer.playAlarm(toneUri)
    }
}

