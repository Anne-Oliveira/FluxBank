package com.example.fluxbank.network

import com.example.fluxbank.network.models.AuthResponse
import com.example.fluxbank.network.models.CadastroRequest
import com.example.fluxbank.network.models.ExtratoResponse
import com.example.fluxbank.network.models.LoginRequest
import com.example.fluxbank.network.models.PixRequest
import com.example.fluxbank.network.models.TransacaoResponse
import com.example.fluxbank.network.models.VerificarTransacaoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface FluxBankApi {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/cadastro")
    suspend fun cadastrar(@Body request: CadastroRequest): Response<AuthResponse>

    @GET("api/extrato/conta/{contaId}/ultimos-30-dias")
    suspend fun buscarExtrato(
        @Path("contaId") contaId: Long,
        @Header("Authorization") token: String
    ): Response<ExtratoResponse>

    @POST("api/pix/transferir")
    suspend fun iniciarPix(
        @Body request: PixRequest,
        @Header("Authorization") token: String
    ): Response<TransacaoResponse>

    @POST("api/pix/verificar")
    suspend fun verificarPix(
        @Body request: VerificarTransacaoRequest,
        @Header("Authorization") token: String
    ): Response<TransacaoResponse>
}