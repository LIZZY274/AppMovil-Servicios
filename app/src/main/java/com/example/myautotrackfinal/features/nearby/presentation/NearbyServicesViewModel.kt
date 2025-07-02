package com.example.myautotrackfinal.features.nearby.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myautotrackfinal.features.nearby.data.LocationRepositoryImpl
import com.example.myautotrackfinal.features.nearby.domain.LocationRepository
import com.example.myautotrackfinal.features.nearby.domain.model.ServiceLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NearbyServicesUiState(
    val isLoading: Boolean = false,
    val services: List<ServiceLocation> = emptyList(),
    val currentLocation: Pair<Double, Double>? = null,
    val error: String? = null,
    val selectedService: ServiceLocation? = null
)

class NearbyServicesViewModel(
    private val locationRepository: LocationRepository = LocationRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(NearbyServicesUiState())
    val uiState: StateFlow<NearbyServicesUiState> = _uiState.asStateFlow()

    init {
        loadNearbyServices()
    }

    fun loadNearbyServices() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val currentLocation = locationRepository.getCurrentLocation()

                if (currentLocation != null) {
                    _uiState.value = _uiState.value.copy(currentLocation = currentLocation)

                    val services = locationRepository.getNearbyServices(
                        latitude = currentLocation.first,
                        longitude = currentLocation.second,
                        radiusKm = 15.0
                    )

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        services = services
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No se pudo obtener la ubicación actual"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar servicios: ${e.localizedMessage}"
                )
            }
        }
    }

    fun selectService(service: ServiceLocation) {
        _uiState.value = _uiState.value.copy(selectedService = service)
    }

    fun clearSelectedService() {
        _uiState.value = _uiState.value.copy(selectedService = null)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}