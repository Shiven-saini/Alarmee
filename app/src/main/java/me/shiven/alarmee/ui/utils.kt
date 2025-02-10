package me.shiven.alarmee.ui

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri

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