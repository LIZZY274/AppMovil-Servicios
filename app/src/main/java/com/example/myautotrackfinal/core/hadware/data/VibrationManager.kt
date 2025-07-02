package com.example.myautotrackfinal.core.hardware.data

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class VibrationManager(private val context: Context) {

    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    fun vibrateSuccess() {
        vibrateWithDuration(100)
    }

    fun vibrateError() {
        vibrateWithDuration(300)
    }

    fun vibrateDelete() {
        val pattern = longArrayOf(0, 150, 100, 150, 100, 300)
        vibrateWithPattern(pattern)
    }

    // ✅ CORREGIDO: Ahora es PÚBLICO
    fun hasVibrator(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.hasVibrator()
        } else {
            @Suppress("DEPRECATION")
            vibrator.hasVibrator()
        }
    }

    private fun vibrateWithDuration(duration: Long) {
        if (!hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrationEffect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(duration)
            }
        } catch (e: Exception) {
            // Silencioso - no es crítico si falla
        }
    }

    private fun vibrateWithPattern(pattern: LongArray) {
        if (!hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrationEffect = VibrationEffect.createWaveform(pattern, -1)
                vibrator.vibrate(vibrationEffect)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(pattern, -1)
            }
        } catch (e: Exception) {
            // Silencioso - no es crítico si falla
        }
    }
}