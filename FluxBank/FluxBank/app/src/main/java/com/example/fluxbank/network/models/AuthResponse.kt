package com.example.fluxbank.network.models

data class AuthResponse(
    val token: String,
    val tipo: String,
    val expiraEm: Long,
    val usuario: UsuarioResponse
)