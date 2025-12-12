package com.example.fluxbank.network

import com.example.fluxbank.network.models.*
import retrofit2.Response
import retrofit2.http.*

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

    @GET("api/pix/buscar-conta")
    suspend fun buscarContaPorChavePix(
        @Query("chavePix") chavePix: String,
        @Header("Authorization") token: String
    ): Response<ContaInfo>

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