package me.shiven.alarmee

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.shiven.alarmee.domain.model.ChallengeList
import me.shiven.alarmee.domain.usecase.GetChallengeSelectedUseCase
import me.shiven.alarmee.domain.usecase.PlayAlarmToneUseCase
import me.shiven.alarmee.domain.usecase.StopAlarmToneUseCase
import me.shiven.alarmee.ui.dismiss.DismissApp
import me.shiven.alarmee.ui.gridchallenge.GridApp
import me.shiven.alarmee.ui.navigation.DismissGraph
import me.shiven.alarmee.ui.nfcchallenge.NFCApp
import me.shiven.alarmee.ui.qrchallenge.QRApp
import me.shiven.alarmee.ui.theme.AlarmeeTheme
import javax.inject.Inject

@AndroidEntryPoint
class DismissActivity : ComponentActivity() {

    @Inject
    lateinit var playAlarmToneUseCase: PlayAlarmToneUseCase

    @Inject
    lateinit var stopAlarmToneUseCase: StopAlarmToneUseCase

    @Inject
    lateinit var getChallengeSelectedUseCase: GetChallengeSelectedUseCase

    private lateinit var startDestination: DismissGraph
    private lateinit var challengeToShow: ChallengeList
    private lateinit var navigateTo: DismissGraph

    // Full-screen wake lock to keep the CPU and screen on.
    private lateinit var wakeLock: PowerManager.WakeLock

    private var isExiting = false

    // Receiver to intercept system dialog events (for example, notification shade interactions).
    private val systemDialogReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
                // Handle system dialog events if needed.
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Ensure the activity shows on the lock screen and wakes the display.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
        // Set flags for full-screen mode, keeping the screen on, and dismissing the keyguard.
        window.addFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        )
        super.onCreate(savedInstanceState)

        // Acquire a full-screen wake lock immediately.
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE,
            "MyApp::DismissWakelock"
        )
        wakeLock.acquire()

        // Increase the alarm stream volume to maximum.
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxAlarmVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxAlarmVolume, 0)

        // Prepare the intent filter for system dialogs.
        val filter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        // For Android 14 (API 34) and higher, specify the receiver export flag.
        val receiverFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Context.RECEIVER_NOT_EXPORTED
        else 0
        registerReceiver(systemDialogReceiver, filter, receiverFlags)

        // Retrieve intent extras.
        val toneReceived = intent?.getStringExtra("toneUri")
        val showChallenge = intent?.getBooleanExtra("toShowChallenge", false)
        val startDestinationString = intent?.getStringExtra("startDestinationID")
        val alarmId = intent?.getIntExtra("alarmId", 100) ?: 100
        NotificationManagerCompat.from(this).cancel(alarmId)


        startDestination = when (startDestinationString) {
            "gridGame" -> DismissGraph.GridAppRoute
            "qrCode"   -> DismissGraph.QRAppRoute
            "nfcTag"   -> DismissGraph.NFCAppRoute
            else       -> DismissGraph.DismissAppRoute
        }

        // Synchronously load the selected challenge.
        challengeToShow = runBlocking { getChallengeSelectedUseCase().first() }

        // Start playing the alarm tone asynchronously.
        lifecycleScope.launch {
            playAlarmToneUseCase(toneReceived)
        }

        navigateTo = when (challengeToShow) {
            ChallengeList.GRID_GAME -> DismissGraph.GridAppRoute
            ChallengeList.NFC_TAG  -> DismissGraph.NFCAppRoute
            ChallengeList.QR_CODE  -> DismissGraph.QRAppRoute
        }

        // Set up the activity's Compose content with navigation.
        setContent {
            val navController = rememberNavController()
            AlarmeeTheme {
                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable<DismissGraph.DismissAppRoute> {
                        DismissApp {
                            if (showChallenge == true) {
                                navController.navigate(navigateTo) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            } else {
                                isExiting = true
                                stopAlarmToneUseCase()
                                this@DismissActivity.finish()
                            }
                        }
                    }
                    composable<DismissGraph.QRAppRoute> {
                        QRApp {
                            isExiting = true
                            stopAlarmToneUseCase()
                            this@DismissActivity.finish()
                        }
                    }
                    composable<DismissGraph.NFCAppRoute> {
                        NFCApp {
                            isExiting = true
                            stopAlarmToneUseCase()
                            this@DismissActivity.finish()
                        }
                    }
                    composable<DismissGraph.GridAppRoute> {
                        GridApp {
                            isExiting = true
                            stopAlarmToneUseCase()
                            this@DismissActivity.finish()
                        }
                    }
                }
            }
        }
    }

    // Override back press to prevent dismissal.
    override fun onBackPressed() {
        Toast.makeText(this, "Not so fast buddy!", Toast.LENGTH_SHORT).show()
    }

    // If the activity loses focus, force it back to the foreground.
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus && !isFinishing && !isExiting) {
            val intent = Intent(this, DismissActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the wake lock if held.
        if (this::wakeLock.isInitialized && wakeLock.isHeld) {
            wakeLock.release()
        }
        unregisterReceiver(systemDialogReceiver)
    }
}

