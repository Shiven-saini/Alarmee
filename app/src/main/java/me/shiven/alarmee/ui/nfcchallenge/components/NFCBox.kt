package me.shiven.alarmee.ui.nfcchallenge.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shiven.alarmee.R

@Composable
fun NFCBox(modifier: Modifier = Modifier, isNFCEnabled: Boolean, onIconClick: () -> Unit) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer) // Uses theme background color for consistency
            .padding(16.dp)
        ,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(isNFCEnabled) {
                Icon(
                    painter = painterResource(R.drawable.nfc),
                    contentDescription = null,
                    Modifier.size(160.dp)
                )
            }
            AnimatedVisibility(!isNFCEnabled) {
                Icon(
                    painter = painterResource(R.drawable.nfc_off),
                    contentDescription = null,
                    Modifier.size(160.dp)
                        .clickable {
                            onIconClick()
                        }
                )
            }

            Spacer(Modifier.height(40.dp))
            Text(
                text = if (isNFCEnabled) "NFC Enabled" else "NFC Disabled",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 30.dp)
            )
            AnimatedVisibility(!isNFCEnabled) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Text(
                        text = "Tap the icon to enable the NFC",
                        fontSize = 14.sp
                    )
                }

            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NFCBoxPreview() {
    NFCBox(isNFCEnabled = false, onIconClick = {})
}