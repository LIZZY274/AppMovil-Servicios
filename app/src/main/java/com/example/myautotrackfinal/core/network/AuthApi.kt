package com.example.myautotrackfinal.core.network

import com.example.myautotrackfinal.features.login.data.model.LoginRequest
import com.example.myautotrackfinal.features.login.data.model.LoginResponse
import com.example.myautotrackfinal.features.register.data.model.RegisterRequest
import com.example.myautotrackfinal.features.register.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}