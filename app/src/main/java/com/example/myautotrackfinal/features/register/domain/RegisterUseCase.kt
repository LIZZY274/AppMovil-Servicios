package com.example.myautotrackfinal.features.register.domain

import com.example.myautotrackfinal.features.register.data.model.RegisterRequest
import com.example.myautotrackfinal.features.register.data.model.RegisterResponse
import com.example.myautotrackfinal.features.register.domain.repository.RegisterRepositoryInterface
import retrofit2.Response

class RegisterUseCase(private val repository: RegisterRepositoryInterface) {
    suspend fun register(request: RegisterRequest): Response<RegisterResponse> {
        return repository.register(request)
    }
}