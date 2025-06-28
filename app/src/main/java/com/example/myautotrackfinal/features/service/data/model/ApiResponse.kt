package com.example.myautotrackfinal.features.service.data.model

data class ServiceApiResponse(
    val message: String,
    val user: String,
    val data: List<Service>
)

data class SingleServiceApiResponse(
    val message: String,
    val user: String,
    val data: Service
)
