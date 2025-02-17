package me.shiven.alarmee.domain.usecase

import android.util.Log
import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.data.repository.AlarmRepository
import me.shiven.alarmee.data.scheduler.AlarmSchedulerImpl
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class AddAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmSchedulerImpl
) {
    suspend operator fun invoke(hour: Int, minute: Int) {
        val isAm: Boolean = if (hour == 12) false else hour < 12
        val randomId = Random.nextInt(1, Int.MAX_VALUE)
        val newAlarm = Alarm(
            id = randomId,
            hour = hour % 12,
            minute = minute,
            isAm = isAm
        )
        println("AddAlarmUseCase : $newAlarm")
        alarmRepository.addAlarm(newAlarm)
        Log.d("Usecase", "Alarm just scheduled!")
        alarmScheduler.scheduleAlarm(newAlarm)
    }
}