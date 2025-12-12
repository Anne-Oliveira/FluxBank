package com.example.fluxbank.network.models

data class VerificarTransacaoRequest(
    val transacaoId: Long,
    val codigoVerificacao: String
)