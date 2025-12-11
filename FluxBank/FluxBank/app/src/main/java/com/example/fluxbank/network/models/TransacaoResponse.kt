package com.example.fluxbank.network.models


data class TransacaoResponse(
    val id: Long,
    val identificadorTransacao: String?,
    val valor: Double,
    val tipoTransacao: String,
    val statusTransacao: String,
    val descricao: String?,
    val nomeDestinatario: String?,
    val criadaEm: String,
    val processadaEm: String?,
    val requerVerificacao: Boolean?,
    val ehEntrada: Boolean?,
    val contaOrigem: String?,
    val contaDestino: String?
)