package com.example.myautotrackfinal.features.register.data.model

data class RegisterResponse(
    val message: String,
    val token: String,
    val user: UserData
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String
)