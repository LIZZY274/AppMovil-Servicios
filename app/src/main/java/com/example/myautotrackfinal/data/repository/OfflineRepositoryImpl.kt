package com.example.myautotrackfinal.data.repository

import com.example.myautotrackfinal.core.database.UserDao
import com.example.myautotrackfinal.core.database.ServiceDao
import com.example.myautotrackfinal.domain.model.*
import com.example.myautotrackfinal.domain.repository.OfflineAuthRepository
import com.example.myautotrackfinal.domain.repository.OfflineServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineAuthRepositoryImpl(
    private val userDao: UserDao
) : OfflineAuthRepository {

    override suspend fun saveUser(user: User) {
        userDao.logoutAllUsers()
        userDao.insertUser(user.toEntity())
    }

    override suspend fun getCurrentUser(): User? {
        return userDao.getLoggedInUser()?.toDomain()
    }

    override suspend fun logout() {
        userDao.logoutAllUsers()
    }

    override suspend fun isLoggedIn(): Boolean {
        return userDao.getLoggedInUser() != null
    }
}

class OfflineServiceRepositoryImpl(
    private val serviceDao: ServiceDao
) : OfflineServiceRepository {

    override fun getServices(userId: String): Flow<List<ServiceOffline>> {
        return serviceDao.getServicesByUser(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveService(service: ServiceOffline) {
        serviceDao.insertService(service.toEntity())
    }

    override suspend fun updateService(service: ServiceOffline) {
        serviceDao.insertService(service.toEntity())
    }

    override suspend fun deleteService(serviceId: String) {
        serviceDao.deleteServiceById(serviceId)
    }

    override suspend fun markAsNeedsSync(serviceId: String, action: SyncAction) {

    }

    override suspend fun getUnsyncedServices(userId: String): List<ServiceOffline> {
        return serviceDao.getUnsyncedServices(userId).map { it.toDomain() }
    }

    override suspend fun markAsSynced(serviceId: String) {
        serviceDao.markAsSynced(serviceId)
    }

    override suspend fun getServiceById(serviceId: String): ServiceOffline? {
        return null
    }
}