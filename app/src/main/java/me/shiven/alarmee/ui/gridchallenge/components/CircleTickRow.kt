package me.shiven.alarmee.ui.gridchallenge.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DoneOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CircleTickRow(
    totalCircles: Int,
    tickedCircles: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalCircles) { index ->
            // Use a distinct background for ticked vs unticked circles
            val circleBackgroundColor = if (index < tickedCircles) {
                // For ticked circles, use a prominent container color
                MaterialTheme.colorScheme.primaryContainer
            } else {
                // For non-ticked circles, use a more subtle container color
                MaterialTheme.colorScheme.surfaceVariant
            }
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(color = circleBackgroundColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = index < tickedCircles,
                    enter = fadeIn(animationSpec = tween(durationMillis = 300)) +
                            scaleIn(initialScale = 0.8f, animationSpec = tween(durationMillis = 300)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300)) +
                            scaleOut(targetScale = 0.8f, animationSpec = tween(durationMillis = 300))
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DoneOutline,
                        contentDescription = "Tick",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCircleTickRow() {
    CircleTickRow(
        totalCircles = 4,
        tickedCircles = 2
    )
}
