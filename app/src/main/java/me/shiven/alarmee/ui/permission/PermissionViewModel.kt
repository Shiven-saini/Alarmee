package me.shiven.alarmee.ui.permission

import android.content.Context
import android.os.Build
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

    fun updatePermissionStatus(context: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            val notificationStatus = NotificationManagerCompat.from(context).areNotificationsEnabled()
            // For Android 14 and above, check full screen intent permission using canUseFullScreenIntent()
            val fullScreenIntentStatus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                NotificationManagerCompat.from(context).canUseFullScreenIntent()
            } else {
                true
            }

            _uiState.update { ui ->
                ui.copy(
                    isNotificationAllowed = notificationStatus,
                    notificationButtonText = if (notificationStatus) "Allowed" else "Allow",
                    isFullScreenIntentAllowed = fullScreenIntentStatus,
                    fullScreenButtonText = if (fullScreenIntentStatus) "Allowed" else "Allow",
                    showProceedButton = notificationStatus && fullScreenIntentStatus,
                    areAllPermissionGranted = notificationStatus && fullScreenIntentStatus,
                )
            }
        }
    }
}

data class PermissionScreenUI(
    var isNotificationAllowed: Boolean = false,
    var notificationButtonText: String = "Allow",
    var isFullScreenIntentAllowed: Boolean = false,
    var fullScreenButtonText: String = "Allow",
    var showProceedButton: Boolean = false,
    var areAllPermissionGranted: Boolean = false
)
