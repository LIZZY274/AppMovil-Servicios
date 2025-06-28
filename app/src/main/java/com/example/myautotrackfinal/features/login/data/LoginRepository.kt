package com.example.myautotrackfinal.features.login.data

import com.example.myautotrackfinal.core.network.AuthApi
import com.example.myautotrackfinal.features.login.data.model.LoginRequest
import com.example.myautotrackfinal.features.login.data.model.LoginResponse
import com.example.myautotrackfinal.features.login.domain.repository.LoginRepositoryInterface
import retrofit2.Response

class LoginRepository(private val authApi: AuthApi) : LoginRepositoryInterface {
    override suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return authApi.login(request)
    }
}