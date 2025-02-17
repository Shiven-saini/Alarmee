package me.shiven.alarmee.data.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.data.receiver.AlarmReceiver
import java.util.Calendar
import java.util.TimeZone


class AlarmSchedulerImpl(
    private val context: Context
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleAlarm(alarm: Alarm) {
        val triggerAtMillis = computeTriggerAtMillis(alarm)
        Log.d("Alarm Scheduler", "Epoch UNIX time => $triggerAtMillis")
        val pendingIntent = createPendingIntent(alarm)
        println(alarm)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
        println("Just scheduled an alarm!")
    }

    fun cancelAlarm(alarm: Alarm) {
        val pendingIntent = createPendingIntent(alarm)
        alarmManager.cancel(pendingIntent)
    }

    fun computeTriggerAtMillis(alarm: Alarm): Long {
        // Use the device's default time zone.
        val timeZone: TimeZone = TimeZone.getDefault()

        // 'now' in the local time zone.
        val now = Calendar.getInstance(timeZone)

        // Create a calendar for the target alarm time and set the time zone explicitly.
        val targetCal = Calendar.getInstance(timeZone).apply {
            // Zero out seconds and milliseconds for exact triggering.
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Convert the 12-hour representation (with isAm flag) into 24-hour format.
        var hourOfDay = alarm.hour
        if (alarm.isAm) {
            if (hourOfDay == 12) hourOfDay = 0
        } else {
            if (hourOfDay != 12) hourOfDay += 12
        }

        // Set the computed hour and minute into the target Calendar.
        targetCal.set(Calendar.HOUR_OF_DAY, hourOfDay)
        targetCal.set(Calendar.MINUTE, alarm.minute)

        // Mapping from day abbreviation to Calendar.DAY_OF_WEEK constant.
        val dayMap = mapOf(
            "Sun" to Calendar.SUNDAY,
            "Mon" to Calendar.MONDAY,
            "Tue" to Calendar.TUESDAY,
            "Wed" to Calendar.WEDNESDAY,
            "Thu" to Calendar.THURSDAY,
            "Fri" to Calendar.FRIDAY,
            "Sat" to Calendar.SATURDAY
        )

        // Parse the repeatDays string to get a list of target days.
        val repeatDaysList = alarm.repeatDays
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        val targetDays = repeatDaysList.mapNotNull { dayMap[it] }

        // If repeat days are provided, determine the next day (including today)
        // on which the alarm should trigger.
        if (targetDays.isNotEmpty()) {
            val currentDay = now.get(Calendar.DAY_OF_WEEK)
            var daysUntilNext = Int.MAX_VALUE

            targetDays.forEach { targetDay ->
                // Calculate day difference from today's day.
                var diff = targetDay - currentDay
                if (diff < 0) diff += 7  // Wrap around if needed.
                // If today is the target day but the target time has already passed, consider next week.
                if (diff == 0 && targetCal.timeInMillis <= now.timeInMillis) {
                    diff = 7
                }
                if (diff < daysUntilNext) {
                    daysUntilNext = diff
                }
            }
            // Advance targetCal by the computed number of days.
            targetCal.add(Calendar.DAY_OF_MONTH, daysUntilNext)
        } else {
            // One-off alarm: if the target time has already passed today, schedule for tomorrow.
            if (targetCal.timeInMillis <= now.timeInMillis) {
                targetCal.add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        // Return the trigger time as UTC epoch milliseconds.
        return targetCal.timeInMillis
    }

    private fun createPendingIntent(alarm: Alarm): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("toneUri", alarm.tone)
            putExtra("toShowChallenge", alarm.challengeStatus)
            putExtra("alarmId", alarm.id)
        }
        return PendingIntent.getBroadcast(
            context,
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}

