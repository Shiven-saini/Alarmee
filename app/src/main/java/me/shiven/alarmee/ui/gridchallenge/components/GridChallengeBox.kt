package me.shiven.alarmee.ui.gridchallenge.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
        onStartChallenge()                  // Start the challenge once composed
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Build the grid row by row
            for (row in 0 until gridSize) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (col in 0 until gridSize) {
                        val tileState = grid[row][col]
                        val tileColor = when (tileState) {
                            TileState.Normal   -> Color.LightGray
                            TileState.Flashing -> Color.Red
                            TileState.Selected -> Color.Green
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f) // Each box takes an equal share of the row's width
                                .aspectRatio(1f) // Ensures the box remains square
                                .background(tileColor, shape = RoundedCornerShape(8.dp))
                                .clickable { onTileClicked(row, col) }
                        )
                    }
                }
            }
        }
    }
}
