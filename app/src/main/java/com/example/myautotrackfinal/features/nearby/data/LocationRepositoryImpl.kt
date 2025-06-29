// features/nearby/data/LocationRepositoryImpl.kt
package com.example.myautotrackfinal.features.nearby.data

import com.example.myautotrackfinal.features.nearby.domain.LocationRepository
import com.example.myautotrackfinal.features.nearby.domain.model.ServiceLocation
import kotlinx.coroutines.delay
import kotlin.math.*

class LocationRepositoryImpl : LocationRepository {

    override suspend fun getCurrentLocation(): Pair<Double, Double>? {
        // Simular obtener ubicación actual
        delay(1000)
        return Pair(16.6148, -93.0938)
    }

    override suspend fun getNearbyServices(
        latitude: Double,
        longitude: Double,
        radiusKm: Double
    ): List<ServiceLocation> {
        delay(1500)


        val mockServices = listOf(

            ServiceLocation(
                id = "1",
                name = "Taller Mecánico El Güero",
                type = "Taller Mecánico",
                latitude = 16.6247,
                longitude = -93.0986,
                address = "Calle Principal 123, Centro, Suchiapa",
                rating = 4.6f,
                phoneNumber = "961-789-0123",
                description = "🔧 Taller familiar con más de 20 años de experiencia - A 2.5 km de la Universidad"
            ),
            ServiceLocation(
                id = "2",
                name = "Auto Eléctrico Hernández",
                type = "Electricidad Automotriz",
                latitude = 16.6227,
                longitude = -93.0976,
                address = "Av. Revolución 45, Suchiapa",
                rating = 4.7f,
                phoneNumber = "961-901-2345",
                description = "⚡ Especialistas en alternadores, marchas y baterías - Muy cerca de la Uni"
            ),
            ServiceLocation(
                id = "3",
                name = "Servicios Automotrices La Esperanza",
                type = "Taller Mecánico",
                latitude = 16.6187,
                longitude = -93.0936,
                address = "Col. La Esperanza, Suchiapa (Muy cerca de la Universidad)",
                rating = 4.5f,
                phoneNumber = "961-345-6789",
                description = "🚗 Servicio completo de mantenimiento - ¡EL MÁS CERCANO A LA UNIVERSIDAD!"
            ),
            ServiceLocation(
                id = "4",
                name = "Llantera Express Universidad",
                type = "Llantera",
                latitude = 16.6168,
                longitude = -93.0958,
                address = "Carretera Universidad Km 1, Suchiapa",
                rating = 4.4f,
                phoneNumber = "961-890-1234",
                description = "🛞 Llantera especializada para estudiantes - Precios especiales con credencial"
            ),
            ServiceLocation(
                id = "5",
                name = "Hojalatería y Pintura San José",
                type = "Hojalatería",
                latitude = 16.6207,
                longitude = -93.0956,
                address = "Barrio San José, Suchiapa",
                rating = 4.3f,
                phoneNumber = "961-123-4567",
                description = "🎨 Trabajo de hojalatería y pintura de calidad - Cerca del campus"
            ),


            ServiceLocation(
                id = "6",
                name = "Taller Diesel Suchiapa",
                type = "Taller Mecánico",
                latitude = 16.6307,
                longitude = -93.1016,
                address = "Entrada principal a Suchiapa, Km 2",
                rating = 4.2f,
                phoneNumber = "961-234-5678",
                description = "🚛 Especialistas en motores diesel y vehículos pesados"
            ),
            ServiceLocation(
                id = "7",
                name = "Refaccionaria Los Hermanos",
                type = "Refacciones",
                latitude = 16.6287,
                longitude = -93.1006,
                address = "Calle Hidalgo 78, Centro Suchiapa",
                rating = 4.1f,
                phoneNumber = "961-012-3456",
                description = "🔩 Refacciones para vehículos nacionales e importados"
            ),
            ServiceLocation(
                id = "8",
                name = "Vulcanizadora Express Carretera",
                type = "Llantera",
                latitude = 16.6407,
                longitude = -93.1074,
                address = "Carretera Tuxtla-Suchiapa Km 18",
                rating = 4.0f,
                phoneNumber = "961-567-8901",
                description = "🔧 Reparación rápida de llantas - Servicio 24 horas"
            ),


            ServiceLocation(
                id = "9",
                name = "AutoServicio García",
                type = "Taller Mecánico",
                latitude = 16.7589,
                longitude = -93.1312,
                address = "Av. Central Poniente 1234, Tuxtla Gutiérrez",
                rating = 4.8f,
                phoneNumber = "961-123-4567",
                description = "🏆 Taller premium en Tuxtla - Especialistas en mantenimiento general"
            ),
            ServiceLocation(
                id = "10",
                name = "Carrocería Chiapas",
                type = "Hojalatería",
                latitude = 16.7609,
                longitude = -93.1252,
                address = "Calle 5a Norte Poniente 890, Tuxtla Gutiérrez",
                rating = 4.9f,
                phoneNumber = "961-345-6789",
                description = "🎨 El mejor taller de carrocería en Tuxtla - Trabajo profesional"
            ),
            ServiceLocation(
                id = "11",
                name = "Llantas del Sur Tuxtla",
                type = "Llantera",
                latitude = 16.7549,
                longitude = -93.1272,
                address = "Blvd. Belisario Domínguez 567, Tuxtla Gutiérrez",
                rating = 4.5f,
                phoneNumber = "961-234-5678",
                description = "🛞 Mayor variedad de llantas en Tuxtla - Todas las marcas"
            ),
            ServiceLocation(
                id = "12",
                name = "Eléctrico Automotriz López",
                type = "Electricidad Automotriz",
                latitude = 16.7529,
                longitude = -93.1332,
                address = "Av. 1a Sur Poniente 321, Tuxtla Gutiérrez",
                rating = 4.4f,
                phoneNumber = "961-456-7890",
                description = "⚡ Especialistas en sistemas eléctricos avanzados"
            ),
            ServiceLocation(
                id = "13",
                name = "Refaccionaria Central Tuxtla",
                type = "Refacciones",
                latitude = 16.7599,
                longitude = -93.1282,
                address = "Calle Central Norte 456, Tuxtla Gutiérrez",
                rating = 4.6f,
                phoneNumber = "961-567-8901",
                description = "🔩 Mayor surtido de refacciones en la región"
            ),


            ServiceLocation(
                id = "14",
                name = "Grúas Universidad 24/7",
                type = "Servicios de Emergencia",
                latitude = 16.6907,
                longitude = -93.1134,
                address = "Carretera Tuxtla-Suchiapa Km 15 (Punto medio)",
                rating = 4.3f,
                phoneNumber = "961-911-2222",
                description = "🚑 Servicio de grúas 24/7 - Especializado en auxiliar estudiantes"
            )
        )


        return mockServices.map { service ->
            val distance = calculateDistance(latitude, longitude, service.latitude, service.longitude)
            service.copy(distance = distance)
        }.filter { it.distance!! <= radiusKm }
            .sortedBy { it.distance }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }
}