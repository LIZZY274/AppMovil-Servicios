package com.example.myautotrackfinal.core.di.module

import android.content.Context
import com.example.myautotrackfinal.core.hardware.data.CameraManager
import com.example.myautotrackfinal.core.hardware.data.CameraRepositoryImpl
import com.example.myautotrackfinal.core.hardware.domain.CameraRepository


object HardwareModule {

    //
    fun provideCameraManager(context: Context): CameraManager {
        return CameraManager(context)
    }

    //
    fun provideCameraRepository(context: Context): CameraRepository {
        val cameraManager = provideCameraManager(context)
        return CameraRepositoryImpl(cameraManager)
    }
}