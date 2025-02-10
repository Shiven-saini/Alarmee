package me.shiven.alarmee.data.model

data class AlarmDefault(
    val id: Int = 0,
    val hour: Int = 6,
    val minute: Int = 30,
    val isAm: Boolean = true,
    val isEnabled: Boolean = true,
    val repeatDays: String = "Mon,Tue,Wed,Thu,Fri",
    val tone: String = "Melody-Chime"
)