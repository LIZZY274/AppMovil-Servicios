package com.example.myautotrackfinal.features.nearby.domain.model

data class ServiceLocation(
    val id: String,
    val name: String,
    val type: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val distance: Double? = null,
    val rating: Float = 0f,
    val isOpen: Boolean = true,
    val phoneNumber: String? = null,
    val description: String? = null
)