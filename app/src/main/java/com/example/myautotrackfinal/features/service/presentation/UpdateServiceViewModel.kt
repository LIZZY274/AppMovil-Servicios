package com.example.myautotrackfinal.features.service.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myautotrackfinal.core.di.AppModule
import com.example.myautotrackfinal.features.service.data.model.Service
import com.example.myautotrackfinal.features.service.data.model.ServiceRequest
import kotlinx.coroutines.launch

class UpdateServiceViewModel(application: Application) : AndroidViewModel(application) {

    private val serviceUseCase = AppModule.provideServiceUseCase(application.applicationContext)


    private val _service = MutableLiveData<Service>()
    val service: LiveData<Service> = _service

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadService(serviceId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val response = serviceUseCase.getServiceById(serviceId)

                if (response.isSuccessful) {
                    val singleServiceResponse = response.body()
                    _service.value = singleServiceResponse?.data
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Error al cargar servicio: $errorBody"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateService(serviceId: String, tipo: String, fecha: String, costo: Double, taller: String, descripcion: String?) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val request = ServiceRequest(tipo, fecha, costo, taller, descripcion)

                val response = serviceUseCase.updateService(serviceId, request)

                if (response.isSuccessful) {
                    _updateSuccess.value = true
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Error al actualizar servicio: $errorBody"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessages() {
        _errorMessage.value = null
        _updateSuccess.value = null
    }
}
