package me.shiven.alarmee.domain.usecase

import kotlinx.coroutines.flow.Flow
import me.shiven.alarmee.domain.dummy.PreferencesRepository
import javax.inject.Inject

// UseCase to check if the on-boarding process has been completed
class CheckOnBoardingCompletedUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.isOnBoardingCompleted
}