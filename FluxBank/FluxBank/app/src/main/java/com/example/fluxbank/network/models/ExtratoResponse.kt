package com.example.fluxbank.network.models

data class ExtratoResponse(
    val numeroConta: String,
    val agencia: String,
    val saldoAtual: Double,
    val dataInicio: String?,
    val dataFim: String?,
    val transacoes: List<TransacaoResponse>,
    val totalTransacoes: Int,
    val totalEntradas: Double?,
    val totalSaidas: Double?
)