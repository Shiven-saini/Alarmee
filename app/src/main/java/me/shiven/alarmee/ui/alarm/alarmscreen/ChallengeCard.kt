package me.shiven.alarmee.ui.alarm.alarmscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shiven.alarmee.ui.theme.AlarmeeTheme

@Composable
fun ChallengeCard(
    onCardClick: () -> Unit,
    onDemoClick: () -> Unit,
    imageVector: ImageVector,
    headerText: String,
    modifier: Modifier = Modifier
) {

    var isSelected by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = if (isSelected) BorderStroke(4.dp, MaterialTheme.colorScheme.primary) else null,
        onClick = {
            isSelected = !isSelected
            onCardClick()
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(80.dp)
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = headerText,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(100.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = headerText,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onDemoClick,
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier.width(120.dp)
                ) {
                    Text(
                        text = "Demo",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Challenge Card Preview - Light")
@Composable
fun ChallengeCardPreviewLight() {
    AlarmeeTheme {
        ChallengeCard(
            onCardClick = { },
            onDemoClick = { },
            imageVector = Icons.Filled.QrCode,
            headerText = "Scan QR Code"
        )
    }
}

@Preview(showBackground = true, name = "Challenge Card Preview - Light")
@Composable
fun ChallengeCardPreviewLight2() {
    AlarmeeTheme {
        ChallengeCard(
            onCardClick = { },
            onDemoClick = { },
            imageVector = Icons.Filled.QrCode,
            headerText = "Grid Game"
        )
    }
}
