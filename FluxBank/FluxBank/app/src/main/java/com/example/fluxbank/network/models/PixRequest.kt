package com.example.fluxbank.network.models

data class PixRequest(
    val contaOrigemId: Long,
    val chavePix: String,
    val valor: Double,
    val descricao: String? = null,
    val requerVerificacao: Boolean = true
)
