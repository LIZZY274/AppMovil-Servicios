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

    private val serviceUseCase = AppModule.provideServiceUseCase(application.applicationContext)
    private val vibrationRepository = AppModule.provideVibrationRepository(application.applicationContext)

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
                    vibrationRepository.vibrateServiceDeleted()
                    loadServices()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Error al eliminar servicio: $errorBody"
                    vibrationRepository.vibrateError()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
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