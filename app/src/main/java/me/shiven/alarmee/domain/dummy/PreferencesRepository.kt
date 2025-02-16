package me.shiven.alarmee.domain.dummy

// Repository interface that defines the contract for DataStore operations
import kotlinx.coroutines.flow.Flow
import me.shiven.alarmee.domain.model.ChallengeList

interface PreferencesRepository {

    // Flow to observe the on-boarding completion status
    val isOnBoardingCompleted: Flow<Boolean>

    // Function to update the on-boarding completion value
    suspend fun updateOnBoardingCompleted(completed: Boolean)

    // Flow to observe the currently selected challenge
    val challengeSelected: Flow<ChallengeList>

    // Function to update the selected challenge
    suspend fun updateChallengeSelected(challenge: ChallengeList)

}
