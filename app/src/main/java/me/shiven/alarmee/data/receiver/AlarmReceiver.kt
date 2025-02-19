package me.shiven.alarmee.data.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import me.shiven.alarmee.DismissActivity
import me.shiven.alarmee.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val toneCollected = intent?.getStringExtra("toneUri")
        val toShowChallenge = intent?.getBooleanExtra("toShowChallenge", false)
        val alarmId = intent?.getIntExtra("alarmId", 100) ?: 100

        context?.let { ctx ->
            // Create an Intent for DismissActivity and add flags to force a fresh launch
            val dismissIntent = Intent(ctx, DismissActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_NO_USER_ACTION or
                            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                )
                putExtra("toneUri", toneCollected)
                putExtra("toShowChallenge", toShowChallenge)
                putExtra("alarmId", alarmId)
            }

            // Wrap the intent in a PendingIntent used with notifications.
            val pendingIntent = PendingIntent.getActivity(
                ctx,
                alarmId,
                dismissIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Build a high-priority notification with fullScreenIntent enabled.
            val notificationBuilder = NotificationCompat.Builder(ctx, "alarmee_1902")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Use a valid icon resource
                .setContentTitle("Alarm Triggered")
                .setContentText("Tap to dismiss the alarm")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                // This is the key: it tells the system to launch the activity in full screen.
                .setFullScreenIntent(pendingIntent, true)
                .setAutoCancel(true)

            // Issue the notification.
            val notificationManager = NotificationManagerCompat.from(ctx)
            notificationManager.notify(alarmId, notificationBuilder.build())
        }
    }
}
