package me.shiven.alarmee.domain.usecase

import me.shiven.alarmee.domain.dummy.PreferencesRepository
import me.shiven.alarmee.domain.model.ChallengeList
import javax.inject.Inject


// UseCase to update the selected challenge option
class UpdateChallengeSelectedUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(challenge: ChallengeList) {
        repository.updateChallengeSelected(challenge)
    }
}