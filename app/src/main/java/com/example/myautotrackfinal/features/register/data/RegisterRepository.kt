package com.example.myautotrackfinal.features.register.data

import com.example.myautotrackfinal.core.network.AuthApi
import com.example.myautotrackfinal.features.register.data.model.RegisterRequest
import com.example.myautotrackfinal.features.register.data.model.RegisterResponse
import com.example.myautotrackfinal.features.register.domain.repository.RegisterRepositoryInterface
import retrofit2.Response

class RegisterRepository(private val authApi: AuthApi) : RegisterRepositoryInterface {
    override suspend fun register(request: RegisterRequest): Response<RegisterResponse> {
        return authApi.register(request)
    }
}