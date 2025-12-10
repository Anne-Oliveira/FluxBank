package com.example.fluxbank.network

import com.example.fluxbank.network.models.AuthResponse
import com.example.fluxbank.network.models.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FluxBankApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

}