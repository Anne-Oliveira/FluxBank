package com.example.fluxbank.network.models

data class TransacaoResponse(
    val id: Long,
    val contaOrigemId: Long?,
    val contaDestinoId: Long?,
    val valor: Double,
    val tipoTransacao: String,
    val statusTransacao: String,
    val nomeDestinatario: String?,
    val documentoDestinatario: String?,
    val descricao: String?,
    val criadaEm: String?,
    val processadaEm: String?,
    val verificada: Boolean?,
    val requerVerificacao: Boolean? = true,
    val ehEntrada: Boolean? = null
)