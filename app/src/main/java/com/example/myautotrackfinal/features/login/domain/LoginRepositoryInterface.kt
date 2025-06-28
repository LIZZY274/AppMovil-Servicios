package com.example.myautotrackfinal.features.login.domain.repository

import com.example.myautotrackfinal.features.login.data.model.LoginRequest
import com.example.myautotrackfinal.features.login.data.model.LoginResponse
import retrofit2.Response

interface LoginRepositoryInterface {
    suspend fun login(request: LoginRequest): Response<LoginResponse>
}