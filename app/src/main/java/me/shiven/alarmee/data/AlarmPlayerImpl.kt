package me.shiven.alarmee.data

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import me.shiven.alarmee.domain.dummy.AlarmSoundPlayer
import javax.inject.Inject

class AlarmSoundPlayerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AlarmSoundPlayer {
    private var mediaPlayer: MediaPlayer? = null

    override fun playAlarm(toneUri: Uri) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, toneUri)
            setAudioStreamType(AudioManager.STREAM_ALARM)
            isLooping = true
            prepare()
            start()
        }
    }

    override fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}