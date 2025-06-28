package com.example.myautotrackfinal.features.service.presentation

import android.Manifest
import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    onPhotoCaptured: (Uri) -> Unit,
    onBackPressed: () -> Unit,
    cameraRepository: com.example.myautotrackfinal.core.hardware.domain.CameraRepository
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // 🔐 Permisos de cámara
    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    var isCameraInitialized by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 📸 Preview view
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    LaunchedEffect(cameraPermissionState.status.isGranted) {
        if (cameraPermissionState.status.isGranted) {
            cameraRepository.initializeCamera(
                lifecycleOwner = lifecycleOwner,
                previewView = previewView,
                onSuccess = { isCameraInitialized = true },
                onError = { error -> errorMessage = error.message }
            )
        }
    }

    if (!cameraPermissionState.status.isGranted) {
        // 🔐 Pantalla de permisos
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Se necesita permiso de cámara para tomar fotos",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = { cameraPermissionState.launchPermissionRequest() }
            ) {
                Text("Conceder permiso")
            }
        }
    } else {
        // 📸 Pantalla de cámara
        Box(modifier = Modifier.fillMaxSize()) {
            // 📱 Vista previa de la cámara
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )

            // ⬅️ Botón de regreso
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color.White
                )
            }

            // 📸 Botón de captura
            FloatingActionButton(
                onClick = {
                    if (isCameraInitialized) {
                        cameraRepository.capturePhoto(
                            onPhotoSaved = { uri ->
                                onPhotoCaptured(uri)
                                onBackPressed()
                            },
                            onError = { error ->
                                errorMessage = error.message
                            }
                        )
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp)
                    .size(80.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.PhotoCamera,
                    contentDescription = "Tomar foto",
                    modifier = Modifier.size(40.dp),
                    tint = Color.White
                )
            }

            // ⚠️ Mostrar errores
            errorMessage?.let { error ->
                Card(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}
