package com.example.myautotrackfinal.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class User(
    val id: String,
    val email: String,
    val name: String,
    val token: String?,
    val isLoggedIn: Boolean = false
) : Parcelable

@Parcelize
data class ServiceOffline(
    val id: String = UUID.randomUUID().toString(),
    val tipo: String,
    val fecha: String,
    val costo: Double,
    val taller: String,
    val descripcion: String?,
    val imagenUrl: String?,
    val userId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val needsSync: Boolean = false,
    val syncAction: SyncAction = SyncAction.CREATE
) : Parcelable

enum class SyncAction {
    CREATE, UPDATE, DELETE
}

// Estados de UI
sealed class UiState<T> {
    data class Success<T>(val data: T) : UiState<T>()
    data class Error<T>(val message: String) : UiState<T>()
    data class Loading<T>(val isLoading: Boolean = true) : UiState<T>()
    class Idle<T> : UiState<T>()
}

// Eventos de UI
sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    object NavigateBack : UiEvent()
    data class Navigate(val route: String) : UiEvent()
}

// Validaciones
object ValidationRules {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidName(name: String): Boolean {
        return name.trim().length >= 2
    }

    fun isValidPrice(price: String): Boolean {
        return try {
            price.toDouble() > 0
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun isValidServiceTitle(title: String): Boolean {
        return title.trim().length >= 3
    }

    fun isValidServiceDescription(description: String): Boolean {
        return description.trim().length >= 10
    }
}