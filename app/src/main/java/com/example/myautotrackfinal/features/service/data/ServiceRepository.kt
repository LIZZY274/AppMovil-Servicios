package com.example.myautotrackfinal.features.service.data

import com.example.myautotrackfinal.core.network.ServiceApi
import com.example.myautotrackfinal.features.service.data.model.ServiceRequest
import com.example.myautotrackfinal.features.service.domain.repository.ServiceRepositoryInterface
import com.example.myautotrackfinal.features.service.domain.ServiceListResponse
import com.example.myautotrackfinal.features.service.domain.SingleServiceResponse
import com.example.myautotrackfinal.features.service.domain.ServiceActionResponse
import retrofit2.Response

class ServiceRepository(private val serviceApi: ServiceApi) : ServiceRepositoryInterface {

    override suspend fun getAllServices(): Response<ServiceListResponse> {
        return serviceApi.getServices("Bearer temp_token")
    }

    override suspend fun getServiceById(id: String): Response<SingleServiceResponse> {
        return serviceApi.getServiceById("Bearer temp_token", id)
    }

    override suspend fun createService(serviceRequest: ServiceRequest): Response<ServiceActionResponse> {
        return serviceApi.createService("Bearer temp_token", serviceRequest)
    }

    override suspend fun updateService(id: String, serviceRequest: ServiceRequest): Response<ServiceActionResponse> {
        return serviceApi.updateService("Bearer temp_token", id, serviceRequest)
    }

    override suspend fun deleteService(id: String): Response<ServiceActionResponse> {
        return serviceApi.deleteService("Bearer temp_token", id)
    }
}