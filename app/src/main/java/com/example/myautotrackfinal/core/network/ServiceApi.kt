package com.example.myautotrackfinal.core.network

import com.example.myautotrackfinal.features.service.data.model.ServiceRequest
import com.example.myautotrackfinal.features.service.data.model.ServiceApiResponse
import com.example.myautotrackfinal.features.service.data.model.SingleServiceApiResponse
import retrofit2.Response
import retrofit2.http.*

interface ServiceApi {

    @GET("api/services")
    suspend fun getAllServices(): Response<ServiceApiResponse>

    @GET("api/services/{id}")
    suspend fun getServiceById(@Path("id") id: String): Response<SingleServiceApiResponse>

    @POST("api/services")
    suspend fun createService(@Body request: ServiceRequest): Response<Void>

    @PUT("api/services/{id}")
    suspend fun updateService(@Path("id") id: String, @Body request: ServiceRequest): Response<Void>

    @DELETE("api/services/{id}")
    suspend fun deleteService(@Path("id") id: String): Response<Void>
}