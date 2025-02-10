package me.shiven.alarmee.domain.usecase

import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.data.repository.AlarmRepository
import me.shiven.alarmee.ui.alarm.daysMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateAlarmDayUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
){
    suspend operator fun invoke(id: Int, alarm: Alarm){
        val repeatDaysList = if(alarm.repeatDays == ""){
            mutableListOf()
        } else {
            alarm.repeatDays.split(',').toMutableList()
        }

        if(repeatDaysList.contains(daysMap[id]!!)){
            repeatDaysList.remove(daysMap[id]!!)
        } else {
            repeatDaysList.add(daysMap[id]!!)
        }
        println(repeatDaysList)
        alarmRepository.updateAlarm(
            alarm.copy(
                repeatDays = repeatDaysList.joinToString(separator = ",")
            )
        )
    }
}