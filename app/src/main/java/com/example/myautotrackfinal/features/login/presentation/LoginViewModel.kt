package com.example.myautotrackfinal.features.login.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myautotrackfinal.core.di.AppModule
import com.example.myautotrackfinal.features.login.data.model.LoginRequest
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {


    private val tokenManager = AppModule.provideTokenManager(application.applicationContext)
    private val loginUseCase = AppModule.provideLoginUseCase(application.applicationContext)

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val request = LoginRequest(email, password)
                val response = loginUseCase.execute(request)

                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (token != null) {
                        tokenManager.saveToken(token) //gr
                        _loginSuccess.value = true
                    } else {
                        _errorMessage.value = "Token no recibido."
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Error al iniciar sesión: $errorBody"
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
        _loginSuccess.value = null
    }
}
