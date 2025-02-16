package me.shiven.alarmee.ui.gridchallenge.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.shiven.alarmee.ui.gridchallenge.TileState

@Composable
fun GridChallengeBox(
    grid: List<List<TileState>>,            // 2D grid of tile states
    gridSize: Int,                          // Size of the grid
    onTileClicked: (Int, Int) -> Unit,        // Callback for tile clicks
    onStartChallenge: () -> Unit            // Callback to start the challenge
) {
    LaunchedEffect(Unit) {
        onStartChallenge()                  // Call the provided callback instead of viewModel.startChallenge()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Build the grid row by row.
            for (row in 0 until gridSize) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (col in 0 until gridSize) {
                        val tileState = grid[row][col]
                        val tileColor = when (tileState) {
                            TileState.Normal   -> Color.LightGray
                            TileState.Flashing -> Color.Cyan
                            TileState.Selected -> Color.Green
                        }
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(tileColor, shape = RoundedCornerShape(8.dp))
                                .clickable { onTileClicked(row, col) }
                        )
                    }
                }
            }
        }
    }
}
