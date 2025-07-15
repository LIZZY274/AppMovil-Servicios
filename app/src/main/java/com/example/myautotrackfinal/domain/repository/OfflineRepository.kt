package com.example.myautotrackfinal.domain.repository

import com.example.myautotrackfinal.domain.model.ServiceOffline
import com.example.myautotrackfinal.domain.model.User
import com.example.myautotrackfinal.domain.model.SyncAction
import kotlinx.coroutines.flow.Flow

interface OfflineAuthRepository {
    suspend fun saveUser(user: User)
    suspend fun getCurrentUser(): User?
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}

interface OfflineServiceRepository {
    fun getServices(userId: String): Flow<List<ServiceOffline>>
    suspend fun saveService(service: ServiceOffline)
    suspend fun updateService(service: ServiceOffline)
    suspend fun deleteService(serviceId: String)
    suspend fun markAsNeedsSync(serviceId: String, action: SyncAction)
    suspend fun getUnsyncedServices(userId: String): List<ServiceOffline>
    suspend fun markAsSynced(serviceId: String)
    suspend fun getServiceById(serviceId: String): ServiceOffline?
}