package com.example.myautotrackfinal.features.service.domain

import com.example.myautotrackfinal.core.network.ServiceApi
import com.example.myautotrackfinal.core.datastore.TokenManager
import com.example.myautotrackfinal.features.service.data.model.Service
import com.example.myautotrackfinal.features.service.data.model.ServiceRequest
import retrofit2.Response


data class ServiceListResponse(
    val services: List<Service>? = null,
    val data: List<Service>? = null,
    val results: List<Service>? = null
)

data class SingleServiceResponse(
    val service: Service? = null,
    val data: Service? = null,
    val result: Service? = null
)

data class ServiceActionResponse(
    val success: Boolean,
    val message: String? = null,
    val data: Service? = null
)

class ServiceUseCase(
    private val serviceRepository: com.example.myautotrackfinal.features.service.domain.repository.ServiceRepositoryInterface
) {

    suspend fun getAllServices(): Response<ServiceListResponse> {
        return serviceRepository.getAllServices()
    }

    suspend fun getServiceById(id: String): Response<SingleServiceResponse> {
        return serviceRepository.getServiceById(id)
    }

    suspend fun addService(serviceRequest: ServiceRequest): Response<ServiceActionResponse> {
        return serviceRepository.createService(serviceRequest)
    }

    suspend fun updateService(id: String, serviceRequest: ServiceRequest): Response<ServiceActionResponse> {
        return serviceRepository.updateService(id, serviceRequest)
    }

    suspend fun deleteService(id: String): Response<ServiceActionResponse> {
        return serviceRepository.deleteService(id)
    }
}