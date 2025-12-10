package com.example.fluxbank.network.models

import java.math.BigDecimal

data class ContaResponse(
    val id: Long,
    val numeroConta: String,
    val agencia: String,
    val saldo: BigDecimal,
    val tipoConta: String,
    val ativa: Boolean,
    val chavePix: String?
)