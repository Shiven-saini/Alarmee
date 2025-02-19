package me.shiven.alarmee.ui.gridchallenge

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.shiven.alarmee.domain.usecase.TriggerVibrationUseCase
import javax.inject.Inject

// Enum to represent each tile's display state.
enum class TileState {
    Normal,
    Flashing,
    Selected
}

// Enum for the grid mode
enum class ChallengeMode {
    FLASHING,
    INPUT
}

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    object FinishActivity : UiEvent()
}

@HiltViewModel
class GridViewModel @Inject constructor(
    private val triggerVibrationUseCase: TriggerVibrationUseCase
) : ViewModel() {
    val gridSize = 3

    private val _circleState = MutableStateFlow(CirclesState())
    val circleState: StateFlow<CirclesState> = _circleState.asStateFlow()

    // UI events channel to communicate with the Activity.
    private val _uiEvent = Channel<UiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    // Initialize grid as a list of mutable lists.
    val grid = (0 until gridSize).map {
        mutableStateListOf<TileState>().apply {
            repeat(gridSize) { add(TileState.Normal) }
        }
    }.toMutableStateList()

    // Predetermined challenge sequence.
    var challengeSequence = listOf(
        0 to 1,
        1 to 2,
        2 to 1,
        1 to 0
    )

    // Tracks the sequence of user taps.
    var userSequence = mutableStateListOf<Pair<Int, Int>>()

    // Controls the current challenge mode: flashing or waiting for input.
    var challengeMode by mutableStateOf(ChallengeMode.FLASHING)
    // Indicates whether the challenge cycle is finally completed.
    var challengeCompleted by mutableStateOf(false)

    fun incrementTickedCircles() {
        _circleState.update { it.copy(tickedCircles = it.tickedCircles + 1) }
        println(circleState.value)
    }


    // Helper to flash a challenge sequence.
    private suspend fun flashSequence() {
        delay(1000)
        for ((row, col) in challengeSequence) {
            grid[row][col] = TileState.Flashing
            delay(500)
            grid[row][col] = TileState.Normal
            delay(250)
        }
    }

    // Starts the challenge flash sequence.
    fun startChallenge() {
        viewModelScope.launch {
            flashSequence()
            challengeMode = ChallengeMode.INPUT
        }
    }

    // Processes a tap on the grid.
    fun onTileClicked(row: Int, col: Int) {
        if (challengeMode != ChallengeMode.INPUT || challengeCompleted) return

        val currentIndex = userSequence.size
        if (currentIndex < challengeSequence.size && challengeSequence[currentIndex] == (row to col)) {
            grid[row][col] = TileState.Selected
            userSequence.add(row to col)
            if (userSequence.size == challengeSequence.size) {
                // Correct sequence completed, update counter.
                incrementTickedCircles()
                println(_circleState.value)
                // Check if further challenges are required.
                if (circleState.value.tickedCircles < circleState.value.totalCircles) {
                    viewModelScope.launch {
                        resetGrid()
                        userSequence.clear()
                        challengeSequence = generateChallengeSequence()
                        challengeMode = ChallengeMode.FLASHING
                        flashSequence()
                        challengeMode = ChallengeMode.INPUT
                    }
                } else {
                    // Final successful completion: mark as complete and show a toast.
                    challengeCompleted = true
                }
            }
        } else {
            resetChallenge()
        }
    }

    // Resets the grid cells to Normal.
    private fun resetGrid() {
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                grid[row][col] = TileState.Normal
            }
        }
    }

    // Resets the challenge on error and restarts the flash sequence.
    private fun resetChallenge() {
        viewModelScope.launch {
            triggerVibrationUseCase()
            resetGrid()
            userSequence.clear()
            challengeSequence = generateChallengeSequence()
            challengeMode = ChallengeMode.FLASHING
            flashSequence()
            challengeMode = ChallengeMode.INPUT
        }
    }

    // Generates a new challenge sequence using random grid positions.
    private fun generateChallengeSequence(): List<Pair<Int, Int>> {
        return List(4) { (0 until gridSize).random() to (0 until gridSize).random() }
    }
}

data class CirclesState(
    val totalCircles: Int = 4,
    val tickedCircles: Int = 0
)
