package me.shiven.alarmee.domain.usecase

import me.shiven.alarmee.domain.dummy.VibrationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TriggerVibrationUseCase @Inject constructor(
    private val repository: VibrationRepository
) {

    operator fun invoke(duration: Long = 1000L) {
        println("Vibration triggered")
        repository.vibrate(duration)
    }

}