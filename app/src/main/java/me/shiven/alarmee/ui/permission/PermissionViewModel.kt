package me.shiven.alarmee.ui.permission

import android.content.Context
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(PermissionScreenUI())
    val uiState: StateFlow<PermissionScreenUI> = _uiState.asStateFlow()

    fun updatePermissionStatus(context: Context) =
        viewModelScope.launch(Dispatchers.Main) {
            val notificationStatus = NotificationManagerCompat.from(context).areNotificationsEnabled()
            val drawOverStatus = Settings.canDrawOverlays(context)
            _uiState.update { ui ->
                ui.copy(
                    isNotificationAllowed = notificationStatus,
                    notificationButtonText = if (notificationStatus) "Allowed" else "Allow",
                    isDrawOverAllowed = drawOverStatus,
                    drawButtonText = if (drawOverStatus) "Allowed" else "Allow",
                    showProceedButton = notificationStatus && drawOverStatus,
                    areAllPermissionGranted = notificationStatus && drawOverStatus,
                )
            }
        }
}


data class PermissionScreenUI(
    var isNotificationAllowed: Boolean = false,
    var notificationButtonText: String = "Allow",
    var isDrawOverAllowed: Boolean = false,
    var drawButtonText: String = "Allow",
    var showProceedButton: Boolean = false,
    var areAllPermissionGranted: Boolean = false
)