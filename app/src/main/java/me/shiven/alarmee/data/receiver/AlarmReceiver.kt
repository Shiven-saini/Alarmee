package me.shiven.alarmee.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import me.shiven.alarmee.DismissActivity

class AlarmReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val toneCollected = intent?.getStringExtra("toneUri")
        val toShowChallenge = intent?.getBooleanExtra("toShowChallenge", false)
        val alarmId = intent?.getIntExtra("alarmId", 100)
        println("Alarm intent received with id : $alarmId")

        context?.let {
            val dismissIntent = Intent(it, DismissActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra("toneUri", toneCollected)
                putExtra("toShowChallenge", toShowChallenge)
            }
            it.startActivity(dismissIntent)
        }
    }
}