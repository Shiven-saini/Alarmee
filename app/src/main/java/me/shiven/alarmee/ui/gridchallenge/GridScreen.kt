package me.shiven.alarmee.ui.gridchallenge

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import me.shiven.alarmee.ui.gridchallenge.components.CircleTickRow
import me.shiven.alarmee.ui.gridchallenge.components.GridChallengeBox
import me.shiven.alarmee.ui.theme.AlarmeeTheme

@Composable
fun GridScreen(viewModel: GridViewModel, onCompletionCallback: () -> Unit, modifier: Modifier = Modifier) {

    val (totalCircles, tickedCircles) = viewModel.circleState.collectAsState().value

    LaunchedEffect(viewModel.challengeCompleted){
        if(viewModel.challengeCompleted) onCompletionCallback()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(20.dp)
    ) {
        Text(
            text = "Challenge: Grid",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.padding(bottom = 30.dp)
        )

        Text(
            text = "Follow the combinations to complete the challenge",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = Modifier.padding(bottom = 50.dp)
        )

        CircleTickRow(totalCircles = totalCircles, tickedCircles = tickedCircles)

        Spacer(
            modifier = Modifier.padding(bottom = 80.dp)
        )

        GridChallengeBox(
            grid = viewModel.grid,
            gridSize = viewModel.gridSize,
            onTileClicked = { row, col -> viewModel.onTileClicked(row, col) },
            onStartChallenge = { viewModel.startChallenge() }
        )
    }
}


//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//private fun QRScannerScreenPreview() {
//    AlarmeeTheme {
//        GridScreen(hiltViewModel<GridViewModel>())
//    }
//}