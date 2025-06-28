package com.example.myautotrackfinal.features.register.domain.repository

import com.example.myautotrackfinal.features.register.data.model.RegisterRequest
import com.example.myautotrackfinal.features.register.data.model.RegisterResponse
import retrofit2.Response

interface RegisterRepositoryInterface {
    suspend fun register(request: RegisterRequest): Response<RegisterResponse>
}