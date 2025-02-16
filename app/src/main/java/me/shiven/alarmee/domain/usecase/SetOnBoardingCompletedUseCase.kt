package me.shiven.alarmee.domain.usecase

import me.shiven.alarmee.domain.dummy.PreferencesRepository
import javax.inject.Inject

// UseCase to update the on-boarding completed status
class SetOnBoardingCompletedUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(completed: Boolean) {
        repository.updateOnBoardingCompleted(completed)
    }
}