package me.shiven.alarmee.data.local.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.shiven.alarmee.domain.model.ChallengeList

@Entity(tableName = "alarm_table")
data class Alarm(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val hour: Int,
    val minute: Int,
    val isAm: Boolean = true,
    val isEnabled: Boolean = true,
    val repeatDays: String = "Mon,Tue,Wed,Thu,Fri",
    val tone: String = "content://settings/system/alarm_alert",
    val challengeStatus: Boolean = false
    
)


// TODO: Update to single challenge instead of different ones for different alarms. 
