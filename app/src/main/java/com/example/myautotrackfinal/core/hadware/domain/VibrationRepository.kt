package com.example.myautotrackfinal.core.hardware.domain

interface VibrationRepository {
    fun vibrateSuccess()
    fun vibrateError()
    fun hasVibrator(): Boolean
}