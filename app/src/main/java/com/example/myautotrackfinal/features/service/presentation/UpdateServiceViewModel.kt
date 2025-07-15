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
import java.text.SimpleDateFormat
import java.util.*

class UpdateServiceViewModel(application: Application) : AndroidViewModel(application) {

    private val serviceUseCase = AppModule.provideServiceUseCase(application.applicationContext)

    private val _service = MutableLiveData<Service?>()
    val service: LiveData<Service?> = _service

    private val _updateSuccess = MutableLiveData<Boolean?>()
    val updateSuccess: LiveData<Boolean?> = _updateSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private fun formatDateForServer(fecha: String): String {
        return try {
            if (fecha.contains("-") && fecha.length == 10) {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = inputFormat.parse(fecha)
                outputFormat.format(date ?: Date())
            } else {
                fecha
            }
        } catch (e: Exception) {
            fecha
        }
    }

    fun loadService(serviceId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val response = serviceUseCase.getServiceById(serviceId)

                if (response.isSuccessful) {
                    val singleServiceResponse = response.body()

                    println("=== RESPUESTA DEL SERVIDOR ===")
                    println("Response body: $singleServiceResponse")
                    println("Response type: ${singleServiceResponse?.javaClass?.simpleName}")


                    val serviceValue = singleServiceResponse?.let { resp ->

                        try {
                            val fields = resp.javaClass.declaredFields
                            fields.forEach { field ->
                                field.isAccessible = true
                                val value = field.get(resp)
                                println("Campo: ${field.name} = $value")
                            }


                            val serviceField = fields.find { field ->
                                field.type == Service::class.java ||
                                        field.name.equals("service", ignoreCase = true) ||
                                        field.name.equals("data", ignoreCase = true) ||
                                        field.name.equals("result", ignoreCase = true)
                            }

                            serviceField?.let { field ->
                                field.isAccessible = true
                                field.get(resp) as? Service
                            }
                        } catch (e: Exception) {
                            println("Error en reflection: ${e.message}")
                            null
                        }
                    }

                    if (serviceValue != null) {
                        _service.value = serviceValue
                    } else {
                        _errorMessage.value = "Error: No se encontró el servicio en la respuesta del servidor"
                    }
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

    fun updateService(
        serviceId: String,
        newTipo: String,
        newFecha: String,
        newCosto: Double,
        newTaller: String,
        newDescripcion: String?
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val currentService = _service.value

                if (currentService == null) {
                    _errorMessage.value = "Error: No se pudo cargar el servicio original"
                    _isLoading.value = false
                    return@launch
                }

                val fechaFormateada = formatDateForServer(newFecha)

                val request = ServiceRequest(
                    tipo = if (newTipo.isNotEmpty()) newTipo else currentService.tipo,
                    fecha = fechaFormateada,
                    costo = if (newCosto > 0) newCosto else currentService.costo,
                    taller = if (newTaller.isNotEmpty()) newTaller else currentService.taller,
                    descripcion = newDescripcion ?: currentService.descripcion,
                    imagenUrl = currentService.imagenUrl
                )

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