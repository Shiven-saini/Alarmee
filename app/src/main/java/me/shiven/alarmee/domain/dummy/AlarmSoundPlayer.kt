package me.shiven.alarmee.domain.dummy

import android.net.Uri

interface AlarmSoundPlayer {
    fun playAlarm(toneUri: Uri)
    fun stopAlarm()
}