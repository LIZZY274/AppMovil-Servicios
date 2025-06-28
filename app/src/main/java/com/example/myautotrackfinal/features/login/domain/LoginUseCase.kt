package com.example.myautotrackfinal.features.login.domain

import com.example.myautotrackfinal.features.login.data.model.LoginRequest
import com.example.myautotrackfinal.features.login.data.model.LoginResponse
import com.example.myautotrackfinal.features.login.domain.repository.LoginRepositoryInterface
import retrofit2.Response

class LoginUseCase(private val repository: LoginRepositoryInterface) {
    suspend fun execute(request: LoginRequest): Response<LoginResponse> {
        return repository.login(request)
    }
}
