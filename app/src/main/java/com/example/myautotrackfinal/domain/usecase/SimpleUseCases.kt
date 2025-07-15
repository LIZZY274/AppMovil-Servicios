package com.example.myautotrackfinal.domain.usecase

import com.example.myautotrackfinal.core.utils.ConnectivityUtil
import com.example.myautotrackfinal.domain.model.ServiceOffline
import com.example.myautotrackfinal.domain.model.SyncAction
import com.example.myautotrackfinal.domain.repository.OfflineServiceRepository
import com.example.myautotrackfinal.features.service.data.model.ServiceRequest
import com.example.myautotrackfinal.features.service.domain.ServiceUseCase
import kotlinx.coroutines.flow.Flow


class SaveServiceOfflineUseCase(
    private val offlineRepository: OfflineServiceRepository,
    private val connectivityUtil: ConnectivityUtil
) {
    suspend operator fun invoke(service: ServiceOffline): Result<ServiceOffline> {
        return try {
            val serviceToSave = service.copy(
                isSynced = false,
                needsSync = true,
                syncAction = SyncAction.CREATE
            )

            offlineRepository.saveService(serviceToSave)
            Result.success(serviceToSave)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class GetOfflineServicesUseCase(
    private val offlineRepository: OfflineServiceRepository
) {
    operator fun invoke(userId: String): Flow<List<ServiceOffline>> {
        return offlineRepository.getServices(userId)
    }
}

class SyncServicesUseCase(
    private val offlineRepository: OfflineServiceRepository,
    private val serviceUseCase: ServiceUseCase,
    private val connectivityUtil: ConnectivityUtil
) {
    suspend operator fun invoke(userId: String): Result<String> {
        if (!connectivityUtil.isInternetAvailable()) {
            return Result.failure(Exception("Sin conexión a internet"))
        }

        return try {
            var syncedCount = 0
            val unsyncedServices = offlineRepository.getUnsyncedServices(userId)

            for (service in unsyncedServices) {
                try {
                    when (service.syncAction) {
                        SyncAction.CREATE -> {

                            val request = ServiceRequest(
                                tipo = service.tipo,
                                fecha = service.fecha,
                                costo = service.costo,
                                taller = service.taller,
                                descripcion = service.descripcion,
                                imagenUrl = service.imagenUrl
                            )
                            val response = serviceUseCase.addService(request)
                            if (response.isSuccessful) {
                                offlineRepository.markAsSynced(service.id)
                                syncedCount++
                            }
                        }
                        SyncAction.UPDATE -> {
                            val request = ServiceRequest(
                                tipo = service.tipo,
                                fecha = service.fecha,
                                costo = service.costo,
                                taller = service.taller,
                                descripcion = service.descripcion,
                                imagenUrl = service.imagenUrl
                            )
                            val response = serviceUseCase.updateService(service.id, request)
                            if (response.isSuccessful) {
                                offlineRepository.markAsSynced(service.id)
                                syncedCount++
                            }
                        }
                        SyncAction.DELETE -> {
                            val response = serviceUseCase.deleteService(service.id)
                            if (response.isSuccessful) {
                                offlineRepository.deleteService(service.id)
                                syncedCount++
                            }
                        }
                    }
                } catch (e: Exception) {
                    continue
                }
            }

            Result.success("$syncedCount servicios sincronizados")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}