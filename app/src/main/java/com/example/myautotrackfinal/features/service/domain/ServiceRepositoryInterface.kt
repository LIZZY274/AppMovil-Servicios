package com.example.myautotrackfinal.features.service.domain.repository

import com.example.myautotrackfinal.features.service.data.model.ServiceRequest
import com.example.myautotrackfinal.features.service.data.model.ServiceApiResponse
import com.example.myautotrackfinal.features.service.data.model.SingleServiceApiResponse
import retrofit2.Response

interface ServiceRepositoryInterface {

    suspend fun getAllServices(): Response<ServiceApiResponse>
    suspend fun getServiceById(id: String): Response<SingleServiceApiResponse>
    suspend fun createService(request: ServiceRequest): Response<Void>
    suspend fun updateService(id: String, request: ServiceRequest): Response<Void>
    suspend fun deleteService(id: String): Response<Void>
}