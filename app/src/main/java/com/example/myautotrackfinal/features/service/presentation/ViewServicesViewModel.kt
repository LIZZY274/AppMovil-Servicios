package com.example.myautotrackfinal.features.service.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myautotrackfinal.core.di.AppModule
import com.example.myautotrackfinal.features.service.data.model.Service
import kotlinx.coroutines.launch

class ViewServicesViewModel(application: Application) : AndroidViewModel(application) {

    // ✅ CORREGIDO: Pasar context al serviceUseCase
    private val serviceUseCase = AppModule.provideServiceUseCase(application.applicationContext)
    // ✅ NUEVO: Agregar VibrationRepository
    private val vibrationRepository = AppModule.provideVibrationRepository(application.applicationContext)

    // ✅ CORREGIDO: Cambiar * por _
    private val _services = MutableLiveData<List<Service>>()
    val services: LiveData<List<Service>> = _services

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> = _deleteSuccess

    init {
        loadServices()
    }

    fun loadServices() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val response = serviceUseCase.getAllServices()

                if (response.isSuccessful) {
                    val serviceApiResponse = response.body()
                    _services.value = serviceApiResponse?.data ?: emptyList()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Error al cargar servicios: $errorBody"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteService(serviceId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val response = serviceUseCase.deleteService(serviceId)

                if (response.isSuccessful) {
                    _deleteSuccess.value = true

                    // ✅ NUEVO: Vibrar cuando se elimina exitosamente un servicio
                    vibrationRepository.vibrateServiceDeleted()

                    loadServices() // Recargar la lista después de eliminar
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Error al eliminar servicio: $errorBody"

                    // ✅ NUEVO: Vibración de error si falla la eliminación
                    vibrationRepository.vibrateError()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"

                // ✅ NUEVO: Vibración de error en caso de excepción
                vibrationRepository.vibrateError()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessages() {
        _errorMessage.value = null
        _deleteSuccess.value = null
    }
}