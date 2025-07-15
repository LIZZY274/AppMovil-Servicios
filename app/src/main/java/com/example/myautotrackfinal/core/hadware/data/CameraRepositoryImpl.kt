package com.example.myautotrackfinal.core.hardware.data

import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.myautotrackfinal.core.hardware.domain.CameraRepository

class CameraRepositoryImpl(
    private val cameraManager: CameraManager
) : CameraRepository {

    override fun initializeCamera(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        cameraManager.initializeCamera(lifecycleOwner, previewView, onSuccess, onError)
    }

    override fun capturePhoto(
        onPhotoSaved: (Uri) -> Unit,
        onError: (Exception) -> Unit
    ) {
        cameraManager.capturePhoto(onPhotoSaved, onError)
    }

    override fun release() {
        cameraManager.release()
    }
}
