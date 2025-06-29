package com.example.myautotrackfinal.features.nearby.domain

import com.example.myautotrackfinal.features.nearby.domain.model.ServiceLocation

interface LocationRepository {
    suspend fun getNearbyServices(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 10.0
    ): List<ServiceLocation>

    suspend fun getCurrentLocation(): Pair<Double, Double>?
}