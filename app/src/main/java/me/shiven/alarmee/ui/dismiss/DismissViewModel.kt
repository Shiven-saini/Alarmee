package me.shiven.alarmee.ui.dismiss

// DismissScreenViewModel.kt
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.shiven.alarmee.domain.usecase.StopAlarmToneUseCase
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DismissScreenViewModel @Inject constructor() : ViewModel() {

    // Expose the greeting, formatted clock time, and date as states.
    var greeting by mutableStateOf(calculateGreeting())
        private set

    var clockTime by mutableStateOf(getFormattedTime())
        private set

    var dateText by mutableStateOf(getFormattedDate())
        private set

    @Inject
    lateinit var stopAlarmToneUseCase: StopAlarmToneUseCase

    init {
        startTimeUpdates()
    }

    private fun startTimeUpdates() {
        viewModelScope.launch {
            while (isActive) {
                // Update all values; if only clockTime is needed, you can update just that.
                clockTime = getFormattedTime()
                greeting = calculateGreeting()
                dateText = getFormattedDate()
                delay(1000L) // wait for one second before updating again
            }
        }
    }

    fun onDismiss() {
        stopAlarmToneUseCase()
    }

    private fun calculateGreeting(): String {
        val currentHour = LocalTime.now().hour
        return when {
            currentHour in 5..11 -> "Good Morning!"
            currentHour in 12..17 -> "Good Afternoon!"
            else -> "Good Evening!"
        }
    }

    private fun getFormattedTime(): String {
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        return LocalTime.now().format(timeFormatter)
    }

    private fun getFormattedDate(): String {
        val dateFormatter = DateTimeFormatter.ofPattern("MMMM dd\neeee")
        return LocalDate.now().format(dateFormatter).uppercase()
    }
}


