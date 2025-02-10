package me.shiven.alarmee.ui.permission.permissionscreen

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import me.shiven.alarmee.R
import me.shiven.alarmee.ui.permission.PermissionViewModel
import me.shiven.alarmee.ui.welcome.welcomescreen.ProceedButton

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    viewModel: PermissionViewModel,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val notificationPermissionState =
        rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(notificationPermissionState.status.isGranted) {
        if (notificationPermissionState.status.isGranted) {
            viewModel.updatePermissionStatus(context)
        }
    }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.updatePermissionStatus(context)
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "App Permissions",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        HorizontalDivider(thickness = 3.dp, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(40.dp))

        Image(
            painter = painterResource(R.drawable.permission),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )

        Spacer(Modifier.height(40.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 10.dp)
        ) {
            Text(
                text = "Notification",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Button(
                onClick = {
                    if (uiState.value.isNotificationAllowed) {
                        Toast.makeText(context, "Permission already granted!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        notificationPermissionState.launchPermissionRequest()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(108.dp)
            ) {
                Text(
                    text = uiState.value.notificationButtonText,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 10.dp)
        ) {
            Text(
                text = "Draw over other apps",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Button(
                onClick = {
                    if (uiState.value.isDrawOverAllowed) {
                        Toast.makeText(context, "Permission already granted!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        val intent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:${context.packageName}")
                        )
                        context.startActivity(intent)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(108.dp)
            ) {
                Text(
                    text = uiState.value.drawButtonText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        AnimatedVisibility(
            visible = uiState.value.areAllPermissionGranted,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ProceedButton(
                onClick = onNavigationClick
            )
        }
    }
}