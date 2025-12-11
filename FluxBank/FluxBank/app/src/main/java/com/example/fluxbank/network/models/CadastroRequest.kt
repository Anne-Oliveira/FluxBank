package com.example.fluxbank.network.models

data class CadastroRequest(
    val nomeCompleto: String,
    val cpf: String? = null,
    val cnpj: String? = null,
    val email: String,
    val senha: String,
    val confirmarSenha: String,
    val telefone: String? = null,
    val dataNascimento: String? = null,
    val tipoUsuario: String
)