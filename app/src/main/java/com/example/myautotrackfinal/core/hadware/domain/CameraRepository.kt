package com.example.myautotrackfinal.core.hardware.domain

import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner

interface CameraRepository {
    fun initializeCamera(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    )

    fun capturePhoto(
        onPhotoSaved: (Uri) -> Unit,
        onError: (Exception) -> Unit
    )

    fun release()
}