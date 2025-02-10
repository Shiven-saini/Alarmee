package me.shiven.alarmee.ui.userinfo.userscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.shiven.alarmee.ui.theme.AlarmeeTheme

@Composable
fun UserInputCard(
    userName: String = "",
    onUserNameChange: (String) -> Unit = {},
    dob: String = "",
    onDobChange: (String) -> Unit = {},
    pickDateLambda: () -> Unit = {},
    proceedLambda: () -> Unit = {},
    modifier: Modifier = Modifier
) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(10.dp)
    ) {
        OutlinedTextField(
            value = userName,
            onValueChange = onUserNameChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(
                    text = "Enter your name",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        OutlinedTextField(
            value = dob,
            onValueChange = onDobChange,
            readOnly = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(
                    text = "Enter your DOB",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                IconButton(onClick = pickDateLambda) {
                    Icon(
                        imageVector = Icons.Filled.EditCalendar,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.padding(top = 10.dp)
        )

        Spacer(Modifier.height(40.dp))
        Button(
            onClick = proceedLambda,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Submit",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview
@Composable
private fun UserInputCardPreview() {
    AlarmeeTheme{
        UserInputCard()
    }
}