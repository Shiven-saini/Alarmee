package me.shiven.alarmee.ui.dismiss.components

import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//@Composable
//fun DismissButton(
//    onDismiss: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var offsetX by remember { mutableStateOf(0f) }
//    var maxDragDistance by remember { mutableStateOf(0f) }
//    val circleSize = 54.dp.to() // Circle size in pixels
//
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(70.dp)
//            .onGloballyPositioned { layoutCoordinates ->
//                maxDragDistance = layoutCoordinates.size.width.toFloat() - circleSize // Adjust for circle size
//            }
//            .background(
//                color = MaterialTheme.colorScheme.surfaceVariant,
//                shape = RoundedCornerShape(35.dp) // Rounded edges for the background
//            )
//            .padding(horizontal = 8.dp) // Padding around the entire component for spacing
//    ) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.CenterStart // Ensures the circle is vertically centered
//        ) {
//            Surface(
//                shape = CircleShape,
//                color = MaterialTheme.colorScheme.primary,
//                modifier = Modifier
//                    .offset { IntOffset(offsetX.roundToInt(), 0) }
//                    .size(54.dp) // Circle size with padding around it for alignment
//                    .draggable(
//                        orientation = Orientation.Horizontal,
//                        state = rememberDraggableState { delta ->
//                            offsetX = (offsetX + delta).coerceIn(0f, maxDragDistance)
//                        },
//                        onDragStopped = {
//                            if (offsetX > maxDragDistance * 0.8f) {
//                                onDismiss()
//                            } else {
//                                offsetX = 0f // Reset position if not fully swiped
//                            }
//                        }
//                    )
//            ) {}
//        }
//        Box(
//            contentAlignment = Alignment.CenterStart,
//            modifier = Modifier.fillMaxSize().padding(start = 80.dp)
//        ) {
//            Text(
//                text = "Swipe right",
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                style = MaterialTheme.typography.bodyMedium,
//                textAlign = TextAlign.Start
//            )
//        }
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun DismissButtonPreview() {
//    MaterialTheme {
//        DismissButton(onDismiss = { /* Handle dismiss */ })
//    }
//}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AnimatedDismissButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 2f else 6f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "elevation"
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .scale(scale)
            .height(56.dp)
            .fillMaxWidth()
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> isPressed = true
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> isPressed = false
                }
                false
            },
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation.dp,
            pressedElevation = 2.dp
        )
    ) {
        Text(
            text = "Dismiss",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
fun AnimatedDismissButtonPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.padding(16.dp)
        ) {
            AnimatedDismissButton(
                onClick = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


