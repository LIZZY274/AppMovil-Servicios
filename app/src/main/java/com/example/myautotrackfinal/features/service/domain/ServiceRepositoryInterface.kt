package com.example.myautotrackfinal.features.service.domain.repository

import com.example.myautotrackfinal.features.service.data.model.ServiceRequest
import com.example.myautotrackfinal.features.service.domain.ServiceListResponse
import com.example.myautotrackfinal.features.service.domain.SingleServiceResponse
import com.example.myautotrackfinal.features.service.domain.ServiceActionResponse
import retrofit2.Response

interface ServiceRepositoryInterface {
    suspend fun getAllServices(): Response<ServiceListResponse>
    suspend fun getServiceById(id: String): Response<SingleServiceResponse>
    suspend fun createService(serviceRequest: ServiceRequest): Response<ServiceActionResponse>
    suspend fun updateService(id: String, serviceRequest: ServiceRequest): Response<ServiceActionResponse>
    suspend fun deleteService(id: String): Response<ServiceActionResponse>
}