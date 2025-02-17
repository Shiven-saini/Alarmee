package me.shiven.alarmee.data.local.alarm

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Alarm::class], exportSchema = false, version = 4)
abstract class AlarmDatabase: RoomDatabase() {

    abstract val alarmDao: AlarmDao

}