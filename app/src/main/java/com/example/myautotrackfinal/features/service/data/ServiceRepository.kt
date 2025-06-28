package com.example.myautotrackfinal.features.service.data

import com.example.myautotrackfinal.core.network.ServiceApi
import com.example.myautotrackfinal.features.service.data.model.ServiceRequest
import com.example.myautotrackfinal.features.service.data.model.ServiceApiResponse
import com.example.myautotrackfinal.features.service.data.model.SingleServiceApiResponse
import com.example.myautotrackfinal.features.service.domain.repository.ServiceRepositoryInterface
import retrofit2.Response

class ServiceRepository(private val serviceApi: ServiceApi) : ServiceRepositoryInterface {


    override suspend fun getAllServices(): Response<ServiceApiResponse> {
        return serviceApi.getAllServices()
    }

    override suspend fun getServiceById(id: String): Response<SingleServiceApiResponse> {
        return serviceApi.getServiceById(id)
    }

    override suspend fun createService(request: ServiceRequest): Response<Void> {
        return serviceApi.createService(request)
    }

    override suspend fun updateService(id: String, request: ServiceRequest): Response<Void> {
        return serviceApi.updateService(id, request)
    }

    override suspend fun deleteService(id: String): Response<Void> {
        return serviceApi.deleteService(id)
    }
}