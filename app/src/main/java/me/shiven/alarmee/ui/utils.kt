package me.shiven.alarmee.ui

import android.annotation.SuppressLint
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.provider.Settings

fun parseRepeatDays(repeatDays: String): Set<Int> {
    val daysMap = mapOf(
        "Sun" to 0,
        "Mon" to 1,
        "Tue" to 2,
        "Wed" to 3,
        "Thu" to 4,
        "Fri" to 5,
        "Sat" to 6
    )
    return repeatDays.split(",").mapNotNull { daysMap[it] }.toSet()
}

fun getToneNameFromUri(uri: Uri, context: Context): String {
    return RingtoneManager.getRingtone(context, uri)?.getTitle(context) ?: "Unknown Tone"
}


@SuppressLint("DefaultLocale")
fun timeFormatHelper(durationInSeconds: Int): String {
    return if(durationInSeconds > 60){
        String.format("%d:%02d", durationInSeconds / 60, durationInSeconds % 60)
    } else {
        durationInSeconds.toString()
    }
}

fun getSystemAnimationScale(context: Context): Float {
    return try {
        Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE
        )
    } catch (e: Settings.SettingNotFoundException) {
        1.0f
    }
}