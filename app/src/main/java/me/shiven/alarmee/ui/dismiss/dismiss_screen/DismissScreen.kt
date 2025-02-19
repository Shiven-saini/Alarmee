package me.shiven.alarmee.ui.dismiss.dismiss_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import me.shiven.alarmee.ui.theme.AlarmeeTheme

//// The main stateless screen accepting all parameters from a ViewModel.
//@Composable
//fun DismissScreen(
//    greeting: String,
//    clockTime: String,
//    dateText: String,
//    onDismiss: () -> Unit
//) {
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colorScheme.surface
//    ) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            // Background gradient
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(
//                        Brush.verticalGradient(
//                            colors = listOf(
//                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
//                                MaterialTheme.colorScheme.surface
//                            )
//                        )
//                    )
//            )
//            // Center content vertically
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
//            ) {
//                GreetingText(greeting)
//                Spacer(modifier = Modifier.height(32.dp))
//                DigitalClock(clockTime)
//                Spacer(modifier = Modifier.height(24.dp))
//                DateDisplay(dateText)
//                Spacer(modifier = Modifier.height(32.dp))
//                DismissButton(onClick = onDismiss)
//            }
//        }
//    }
//}
//
//@Composable
//private fun GreetingText(greeting: String) {
//    Text(
//        text = greeting,
//        style = MaterialTheme.typography.headlineSmall,
//        color = MaterialTheme.colorScheme.primary,
//        fontSize = 38.sp,
//        fontWeight = MaterialTheme.typography.headlineSmall.fontWeight
//    )
//}
//
//@Composable
//private fun DigitalClock(clockTime: String) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth(0.8f)
//            .padding(horizontal = 16.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.secondaryContainer
//        ),
//        shape = RoundedCornerShape(16.dp)
//    ) {
//        // Center the text vertically inside the Card
//        Box(modifier = Modifier.fillMaxWidth()) {
//            Text(
//                text = clockTime,
//                modifier = Modifier
//                    .padding(16.dp)
//                    .align(Alignment.Center),
//                fontSize = 48.sp,
//                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
//                color = MaterialTheme.colorScheme.onSecondaryContainer,
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}
//
//@Composable
//private fun DateDisplay(dateText: String) {
//    Text(
//        text = dateText,
//        style = MaterialTheme.typography.bodyLarge,
//        color = MaterialTheme.colorScheme.onSurface,
//        textAlign = TextAlign.Center
//    )
//}
//
//@Composable
//private fun DismissButton(onClick: () -> Unit) {
//    Button(
//        onClick = onClick,
//        modifier = Modifier
//            .fillMaxWidth(0.6f)
//            .height(56.dp),
//        shape = RoundedCornerShape(28.dp),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = MaterialTheme.colorScheme.primary,
//            contentColor = MaterialTheme.colorScheme.onPrimary
//        ),
//        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp)
//    ) {
//        Icon(
//            imageVector = Icons.Default.CheckCircle,
//            contentDescription = "Dismiss Icon",
//            modifier = Modifier.size(24.dp)
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Text(text = "Dismiss", style = MaterialTheme.typography.bodyLarge)
//    }
//}


// Main stateless screen with animations and a modern look.
@Composable
fun DismissScreen(
    greeting: String,
    clockTime: String,
    dateText: String,
    onDismiss: () -> Unit
) {
// Separate visibility flags to stagger the element animations
    var visibleGreeting by remember { mutableStateOf(false) }
    var visibleClock by remember { mutableStateOf(false) }
    var visibleDate by remember { mutableStateOf(false) }
    var visibleButton by remember { mutableStateOf(false) }

// Launch a coroutine to update each visibility flag one after the other.
    LaunchedEffect(Unit) {
        delay(100)
        visibleGreeting = true
        delay(150)
        visibleClock = true
        delay(150)
        visibleDate = true
        delay(150)
        visibleButton = true
    }

// Root container: animated background underneath the content.
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedBackground()
        // Foreground content arranged in a centered Column.
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            AnimatedVisibility(
                visible = visibleGreeting,
                enter = fadeIn(animationSpec = tween(durationMillis = 600))
            ) {
                GreetingText(greeting)
            }
            Spacer(modifier = Modifier.height(24.dp))
            AnimatedVisibility(
                visible = visibleClock,
                enter = fadeIn(animationSpec = tween(durationMillis = 600))
            ) {
                DigitalClock(clockTime)
            }
            Spacer(modifier = Modifier.height(24.dp))
            AnimatedVisibility(
                visible = visibleButton,
                enter = fadeIn(animationSpec = tween(durationMillis = 600))
            ) {
                DismissButton(onClick = onDismiss)
            }
        }
    }

}

// An animated background with a subtle vertical gradient that gently shifts
@Composable
private fun AnimatedBackground() {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedColorStart by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
        targetValue = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val animatedColorEnd by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.95f),
        targetValue = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.85f),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(animatedColorStart, animatedColorEnd)
                )
            )
    )

}

// Displays a modern greeting text.
@Composable
private fun GreetingText(greeting: String) {
    Text(
        text = greeting,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        ),
        color = MaterialTheme.colorScheme.onPrimary
    )
}

// Displays the digital clock inside a Card with modern styling.
@Composable
private fun DigitalClock(clockTime: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = clockTime,
                modifier = Modifier.padding(vertical = 16.dp),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Displays the date with updated typography.
@Composable
private fun DateDisplay(dateText: String) {
    Text(
        text = dateText,
        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center
    )
}

// The modern Dismiss button with an icon and a smooth elevation.
@Composable
private fun DismissButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(56.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
    ) {
        Text(text = "Dismiss", fontSize = 18.sp, style = MaterialTheme.typography.labelLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun DismissScreenPreview() {
    AlarmeeTheme {
        DismissScreen(
            greeting = "Good Morning!",
            clockTime = "07:45 AM",
            dateText = "FEBRUARY 13\nTHURSDAY",
            onDismiss = {}
        )
    }
}
