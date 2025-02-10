package me.shiven.alarmee.data.repository

import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.data.local.alarm.AlarmDao
import javax.inject.Inject

class AlarmRepository @Inject constructor(private val alarmDao: AlarmDao) {

    suspend fun addAlarm(alarm: Alarm) = alarmDao.insert(alarm)

    suspend fun updateAlarm(alarm: Alarm) = alarmDao.update(alarm)

    suspend fun deleteAlarm(alarm: Alarm) = alarmDao.delete(alarm)

    suspend fun getAlarmById(id: Int) = alarmDao.getAlarmById(id)

    suspend fun getAlarmCount() = alarmDao.getAlarmCount()

    // returns boolean if an alarm is present or not.
    suspend fun isAlarmListEmpty() : Boolean {
        return alarmDao.getAlarmCount() == 0
    }

    // returns kotlin flow object to give a flow list of all the available alarms.
    fun getAllAlarms() = alarmDao.getAllAlarms()

}