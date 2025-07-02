package com.example.myautotrackfinal.core.hardware.data

import com.example.myautotrackfinal.core.hardware.domain.VibrationRepository

class VibrationRepositoryImpl(
    private val vibrationManager: VibrationManager
) : VibrationRepository {

    override fun vibrateSuccess() {
        vibrationManager.vibrateSuccess()
    }

    override fun vibrateError() {
        vibrationManager.vibrateError()
    }

    override fun hasVibrator(): Boolean {
        return vibrationManager.hasVibrator()
    }

    fun vibrateServiceDeleted() {
        vibrationManager.vibrateDelete()
    }
}
