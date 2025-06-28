package com.example.myautotrackfinal.core.network

import com.example.myautotrackfinal.core.datastore.TokenManager
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()


        val token = tokenManager.getToken()

        return if (token != null) {

            val authenticatedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()


            val response = chain.proceed(authenticatedRequest)


            if (response.code == 401) {
                tokenManager.deleteToken()
            }

            response
        } else {

            chain.proceed(originalRequest)
        }
    }
}