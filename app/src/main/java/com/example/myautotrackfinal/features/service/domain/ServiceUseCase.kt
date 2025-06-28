package com.example.myautotrackfinal.features.service.domain

import com.example.myautotrackfinal.features.service.data.model.ServiceRequest
import com.example.myautotrackfinal.features.service.data.model.ServiceApiResponse
import com.example.myautotrackfinal.features.service.data.model.SingleServiceApiResponse
import com.example.myautotrackfinal.features.service.domain.repository.ServiceRepositoryInterface
import retrofit2.Response

class ServiceUseCase(private val repository: ServiceRepositoryInterface) {


    suspend fun getAllServices(): Response<ServiceApiResponse> {
        return repository.getAllServices()
    }

    suspend fun getServiceById(id: String): Response<SingleServiceApiResponse> {
        return repository.getServiceById(id)
    }

    suspend fun createService(request: ServiceRequest): Response<Void> {
        return repository.createService(request)
    }

    suspend fun updateService(id: String, request: ServiceRequest): Response<Void> {
        return repository.updateService(id, request)
    }

    suspend fun deleteService(id: String): Response<Void> {
        return repository.deleteService(id)
    }
}