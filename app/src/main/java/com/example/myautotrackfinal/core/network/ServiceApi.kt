package com.example.myautotrackfinal.core.network

import com.example.myautotrackfinal.features.service.data.model.ServiceRequest
import com.example.myautotrackfinal.features.service.domain.ServiceListResponse
import com.example.myautotrackfinal.features.service.domain.SingleServiceResponse
import com.example.myautotrackfinal.features.service.domain.ServiceActionResponse
import retrofit2.Response
import retrofit2.http.*

interface ServiceApi {

    @GET("services")
    suspend fun getServices(
        @Header("Authorization") token: String
    ): Response<ServiceListResponse>

    @GET("services/{id}")
    suspend fun getServiceById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<SingleServiceResponse>

    @POST("services")
    suspend fun createService(
        @Header("Authorization") token: String,
        @Body serviceRequest: ServiceRequest
    ): Response<ServiceActionResponse>

    @PUT("services/{id}")
    suspend fun updateService(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body serviceRequest: ServiceRequest
    ): Response<ServiceActionResponse>

    @DELETE("services/{id}")
    suspend fun deleteService(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ServiceActionResponse>
}