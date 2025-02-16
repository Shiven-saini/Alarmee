package me.shiven.alarmee.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import me.shiven.alarmee.domain.dummy.PreferencesRepository
import me.shiven.alarmee.domain.model.ChallengeList
import javax.inject.Inject
import javax.inject.Singleton

// Extension property to create the DataStore instance
private val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesRepository {

    private object PreferencesKeys {
        val ON_BOARDING_COMPLETED = booleanPreferencesKey("is_on_boarding_completed")
        val CHALLENGE_SELECTED = stringPreferencesKey("challenge_selected")
    }

    // Retrieve the on-boarding status with a default of false
    override val isOnBoardingCompleted: Flow<Boolean> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            preferences[PreferencesKeys.ON_BOARDING_COMPLETED] ?: false
        }

    // Update the on-boarding status value in DataStore
    override suspend fun updateOnBoardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ON_BOARDING_COMPLETED] = completed
        }
    }

    // Retrieve the selected challenge, stored as a string in DataStore
    override val challengeSelected: Flow<ChallengeList> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            val challengeString = preferences[PreferencesKeys.CHALLENGE_SELECTED] ?: ChallengeList.GRID_GAME.name
            try {
                ChallengeList.valueOf(challengeString)
            } catch (e: Exception) {
                ChallengeList.GRID_GAME
            }
        }

    // Update the selected challenge value, saving the enum name in DataStore
    override suspend fun updateChallengeSelected(challenge: ChallengeList) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CHALLENGE_SELECTED] = challenge.name
        }
    }

}
