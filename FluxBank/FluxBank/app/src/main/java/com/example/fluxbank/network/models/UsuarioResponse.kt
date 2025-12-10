package com.example.fluxbank.network.models

data class UsuarioResponse(
    val id: Long,
    val nome: String,
    val cpf: String?,
    val cnpj: String?,
    val email: String,
    val telefone: String?,
    val dataNascimento: String?,
    val tipoUsuario: String,
    val contas: List<ContaResponse>?
    )