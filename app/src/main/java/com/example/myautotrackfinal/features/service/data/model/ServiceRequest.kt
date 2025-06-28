package com.example.myautotrackfinal.features.service.data.model

data class ServiceRequest(
    val tipo: String,
    val fecha: String,
    val costo: Double,
    val taller: String,
    val descripcion: String? = null,
    val imagenUrl: String? = null
)
