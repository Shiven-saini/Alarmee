package me.shiven.alarmee.data.repository

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import me.shiven.alarmee.domain.dummy.VibrationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VibrationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : VibrationRepository {

    override fun vibrate(duration: Long) {
        println("Inside vibrate function")
        val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= 31) {
            // For Android 12 (API 31) and above, use VibratorManager to obtain the default Vibrator.
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            // Fallback to the older service for devices below API 31.
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }

        vibrator?.let {
            println("Inside let block vibrate")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                it.vibrate(duration)
            }
        }
    }
}