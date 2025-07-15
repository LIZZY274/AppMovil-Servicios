package com.example.myautotrackfinal.features.service.presentation

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myautotrackfinal.core.di.AppModule
import com.example.myautotrackfinal.core.hardware.domain.CameraRepository
import com.example.myautotrackfinal.domain.model.ServiceOffline
import com.example.myautotrackfinal.features.service.data.model.ServiceRequest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddServiceViewModel(application: Application) : AndroidViewModel(application) {

    private val serviceUseCase = AppModule.provideServiceUseCase(application.applicationContext)
    private val cameraRepository = AppModule.provideCameraRepository(application.applicationContext)


    private val saveServiceOfflineUseCase = AppModule.provideSaveServiceOfflineUseCase(application)
    private val offlineAuthRepository = AppModule.provideOfflineAuthRepository(application)
    private val connectivityUtil = AppModule.provideConnectivityUtil(application)


    private val _addServiceSuccess = MutableLiveData<Boolean>()
    val addServiceSuccess: LiveData<Boolean> = _addServiceSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showCamera = MutableLiveData<Boolean>()
    val showCamera: LiveData<Boolean> = _showCamera

    private val _capturedImageUri = MutableLiveData<Uri?>()
    val capturedImageUri: LiveData<Uri?> = _capturedImageUri


    fun getCameraRepository(): CameraRepository = cameraRepository

    fun showCamera() {
        _showCamera.value = true
    }

    fun hideCamera() {
        _showCamera.value = false
    }

    fun setImageUri(uri: Uri) {
        _capturedImageUri.value = uri
    }

    fun removeImage() {
        _capturedImageUri.value = null
    }

    private fun formatDateForServer(fecha: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(fecha)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            fecha
        }
    }

    // === MÉTODO PRINCIPAL MEJORADO ===
    fun addService(tipo: String, fecha: String, costo: Double, taller: String, descripcion: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val currentUser = offlineAuthRepository.getCurrentUser()
                if (currentUser == null) {
                    _errorMessage.value = "Error: Usuario no encontrado. Inicia sesión nuevamente."
                    return@launch
                }

                val imagenUrl = _capturedImageUri.value?.toString()
                val fechaFormateada = formatDateForServer(fecha)

                if (connectivityUtil.isInternetAvailable()) {

                    try {
                        val request = ServiceRequest(
                            tipo = tipo,
                            fecha = fechaFormateada,
                            costo = costo,
                            taller = taller,
                            descripcion = descripcion,
                            imagenUrl = imagenUrl
                        )

                        val response = serviceUseCase.addService(request)

                        if (response.isSuccessful) {

                            val serviceOffline = ServiceOffline(
                                tipo = tipo,
                                fecha = fechaFormateada,
                                costo = costo,
                                taller = taller,
                                descripcion = descripcion,
                                imagenUrl = imagenUrl,
                                userId = currentUser.id,
                                isSynced = true,
                                needsSync = false
                            )
                            saveServiceOfflineUseCase(serviceOffline)
                            _addServiceSuccess.value = true
                        } else {

                            saveServiceOfflineAndNotify(
                                tipo, fechaFormateada, costo, taller, descripcion, imagenUrl, currentUser.id
                            )
                        }
                    } catch (e: Exception) {

                        saveServiceOfflineAndNotify(
                            tipo, fechaFormateada, costo, taller, descripcion, imagenUrl, currentUser.id
                        )
                    }
                } else {

                    saveServiceOfflineAndNotify(
                        tipo, fechaFormateada, costo, taller, descripcion, imagenUrl, currentUser.id
                    )
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun saveServiceOfflineAndNotify(
        tipo: String,
        fecha: String,
        costo: Double,
        taller: String,
        descripcion: String,
        imagenUrl: String?,
        userId: String
    ) {
        val serviceOffline = ServiceOffline(
            tipo = tipo,
            fecha = fecha,
            costo = costo,
            taller = taller,
            descripcion = descripcion,
            imagenUrl = imagenUrl,
            userId = userId,
            isSynced = false,
            needsSync = true
        )

        val result = saveServiceOfflineUseCase(serviceOffline)
        if (result.isSuccess) {
            _addServiceSuccess.value = true
        } else {
            _errorMessage.value = "Error al guardar: ${result.exceptionOrNull()?.message}"
        }
    }

    fun clearMessages() {
        _errorMessage.value = null
        _addServiceSuccess.value = null
    }

    override fun onCleared() {
        super.onCleared()
        cameraRepository.release()
    }
}