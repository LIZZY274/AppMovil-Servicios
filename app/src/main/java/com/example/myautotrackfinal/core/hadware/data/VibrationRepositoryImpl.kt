package com.example.myautotrackfinal.core.hardware.data

import com.example.myautotrackfinal.core.hardware.domain.VibrationRepository

class VibrationRepositoryImpl(
    private val vibrationManager: VibrationManager
) : VibrationRepository {

    override fun vibrateSuccess() {
        vibrationManager.vibrateSuccess()
    }

    override fun vibrateAlert() {
        vibrationManager.vibrateAlert()
    }

    override fun vibrateError() {
        vibrationManager.vibrateError()
    }

    override fun vibrateCustom(duration: Long) {
        vibrationManager.vibrateCustom(duration)
    }

    override fun vibratePattern(pattern: LongArray) {
        vibrationManager.vibratePattern(pattern)
    }

    override fun hasVibrator(): Boolean {
        return vibrationManager.hasVibrator()
    }

    /**
     * Método específico para eliminar servicios
     * Usa un patrón especial: corto-pausa-corto-pausa-largo
     */
    fun vibrateServiceDeleted() {
        vibrationManager.vibrateDelete()
    }
}