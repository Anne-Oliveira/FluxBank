package com.example.fluxbank.network.models

data class ContatoRecente(
    val nome: String?,
    val chavePix: String,
    val documento: String?,
    val documentoMascarado: String?,
    val tipoDocumento: String?,
    val instituicao: String = "FluxBank"
)