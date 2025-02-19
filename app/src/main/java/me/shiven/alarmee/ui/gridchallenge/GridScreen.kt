package me.shiven.alarmee.ui.gridchallenge

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.shiven.alarmee.ui.gridchallenge.components.CircleTickRow
import me.shiven.alarmee.ui.gridchallenge.components.GridChallengeBox


@Composable
fun GridScreen(
    viewModel: GridViewModel,
    onCompletionCallback: () -> Unit,
    modifier: Modifier = Modifier
) {

    val (totalCircles, tickedCircles) = viewModel.circleState.collectAsState().value

    LaunchedEffect(viewModel.challengeCompleted) {
        if (viewModel.challengeCompleted) onCompletionCallback()
    }

// Surface provides the modern container in Material 3.
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .padding(top = 50.dp)
        ) {

            // Title text using Material 3 typography style.
            Text(
                text = "Challenge: Grid",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Use Spacer with height for clear, modern spacing.
            Spacer(modifier = Modifier.height(30.dp))

            // Instruction text with Material 3 body styling.
            Text(
                text = "Follow the combinations to complete the challenge",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(50.dp))

            // This remains unchanged.
            CircleTickRow(totalCircles = totalCircles, tickedCircles = tickedCircles)

            Spacer(modifier = Modifier.height(80.dp))

            // The grid box remains intact.
            GridChallengeBox(
                grid = viewModel.grid,
                gridSize = viewModel.gridSize,
                onTileClicked = { row, col -> viewModel.onTileClicked(row, col) },
                onStartChallenge = { viewModel.startChallenge() }
            )
        }
    }
}


//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//private fun QRScannerScreenPreview() {
//    AlarmeeTheme {
//        GridScreen(hiltViewModel<GridViewModel>())
//    }
//}