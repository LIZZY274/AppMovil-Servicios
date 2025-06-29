package com.example.myautotrackfinal.core.hardware.data

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi

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

    fun vibrateAlert() {
        vibrateWithDuration(200)
    }


    fun vibrateError() {
        vibrateWithDuration(300)
    }


    fun vibrateCustom(duration: Long) {
        vibrateWithDuration(duration)
    }


    fun vibrateDelete() {
        val pattern = longArrayOf(0, 150, 100, 150, 100, 300) // [espera, vibra, espera, vibra, espera, vibra]
        vibrateWithPattern(pattern)
    }

    //Vibración con patrón personalizado

    fun vibratePattern(pattern: LongArray) {
        vibrateWithPattern(pattern)
    }


     // Verificar si el dispositivo tiene vibrador

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

            android.util.Log.w("VibrationManager", "Error al vibrar: ${e.message}")
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

            android.util.Log.w("VibrationManager", "Error al vibrar con patrón: ${e.message}")
        }
    }


    fun cancelVibration() {
        try {
            vibrator.cancel()
        } catch (e: Exception) {
            android.util.Log.w("VibrationManager", "Error al cancelar vibración: ${e.message}")
        }
    }
}