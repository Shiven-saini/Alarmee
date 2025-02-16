package me.shiven.alarmee.domain.usecase

import kotlinx.coroutines.flow.Flow
import me.shiven.alarmee.domain.dummy.PreferencesRepository
import me.shiven.alarmee.domain.model.ChallengeList
import javax.inject.Inject


// UseCase to retrieve the selected challenge option
class GetChallengeSelectedUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    operator fun invoke(): Flow<ChallengeList> = repository.challengeSelected
}