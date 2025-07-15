package com.example.myautotrackfinal.domain.model

import com.example.myautotrackfinal.core.database.ServiceEntity
import com.example.myautotrackfinal.core.database.UserEntity


fun UserEntity.toDomain(): User {
    return User(
        id = id,
        email = email,
        name = name,
        token = token,
        isLoggedIn = isLoggedIn
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        name = name,
        token = token,
        isLoggedIn = isLoggedIn
    )
}

// Service Mappers
fun ServiceEntity.toDomain(): ServiceOffline {
    return ServiceOffline(
        id = id,
        tipo = tipo,
        fecha = fecha,
        costo = costo,
        taller = taller,
        descripcion = descripcion,
        imagenUrl = imagenUrl,
        userId = userId,
        createdAt = createdAt,
        isSynced = isSynced,
        needsSync = needsSync,
        syncAction = when (syncAction) {
            "CREATE" -> SyncAction.CREATE
            "UPDATE" -> SyncAction.UPDATE
            "DELETE" -> SyncAction.DELETE
            else -> SyncAction.CREATE
        }
    )
}

fun ServiceOffline.toEntity(): ServiceEntity {
    return ServiceEntity(
        id = id,
        tipo = tipo,
        fecha = fecha,
        costo = costo,
        taller = taller,
        descripcion = descripcion,
        imagenUrl = imagenUrl,
        userId = userId,
        createdAt = createdAt,
        isSynced = isSynced,
        needsSync = needsSync,
        syncAction = syncAction.name
    )
}