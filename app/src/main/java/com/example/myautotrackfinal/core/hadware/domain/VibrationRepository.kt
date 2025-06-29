package com.example.myautotrackfinal.core.hardware.domain

interface VibrationRepository {
    /**
     * Vibración suave para acciones exitosas (100ms)
     */
    fun vibrateSuccess()

    /**
     * Vibración media para alertas (200ms)
     */
    fun vibrateAlert()

    /**
     * Vibración fuerte para errores (300ms)
     */
    fun vibrateError()

    /**
     * Vibración personalizada
     * @param duration Duración en milisegundos
     */
    fun vibrateCustom(duration: Long)

    /**
     * Vibración con patrón (ej: para eliminaciones)
     * @param pattern Array de duración [espera, vibra, espera, vibra...]
     */
    fun vibratePattern(pattern: LongArray)

    /**
     * Verificar si el dispositivo tiene vibrador
     */
    fun hasVibrator(): Boolean
}