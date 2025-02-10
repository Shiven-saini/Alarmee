package me.shiven.alarmee.ui.userinfo.userscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import me.shiven.alarmee.R
import me.shiven.alarmee.ui.welcome.welcomescreen.ProceedButton

@ExperimentalMaterial3Api
@Composable
fun UserScreen(onNavigationClick: () -> Unit, modifier: Modifier = Modifier) {
    var userName by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var showProceed by remember { mutableStateOf(false) }

    val calendarState = rememberUseCaseState()

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            style = CalendarStyle.MONTH
        ),
        selection = CalendarSelection.Date { date ->
            dob = date.toString()
        }
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "User Credentials",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        HorizontalDivider(Modifier.fillMaxWidth(), thickness = 3.dp)
        Spacer(Modifier.height(40.dp))
        Image(
            painter = painterResource(R.drawable.user_info),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
        Spacer(Modifier.height(40.dp))
        UserInputCard(
            userName = userName,
            onUserNameChange = {
                userName = it
            },
            dob = dob,
            onDobChange = {
                dob = it
            },
            pickDateLambda = { calendarState.show() },
            proceedLambda = {showProceed = !showProceed}
        )
        Spacer(Modifier.height(80.dp))

        AnimatedVisibility(
            visible = showProceed,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ProceedButton(
                onClick = onNavigationClick
            )
        }
    }
}