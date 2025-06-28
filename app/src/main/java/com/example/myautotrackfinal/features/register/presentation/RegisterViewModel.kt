package com.example.myautotrackfinal.features.register.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myautotrackfinal.core.di.AppModule
import com.example.myautotrackfinal.features.register.data.model.RegisterRequest
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val tokenManager = AppModule.provideTokenManager(application.applicationContext)
    private val registerUseCase = AppModule.provideRegisterUseCase(application.applicationContext)

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val request = RegisterRequest(name, email, password)
                val response = registerUseCase.register(request)

                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (token != null) {
                        tokenManager.saveToken(token)
                        _registerSuccess.value = true
                    } else {
                        _errorMessage.value = "Token no recibido."
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Error al registrarse: $errorBody"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessages() {
        _errorMessage.value = null
        _registerSuccess.value = null
    }
}
