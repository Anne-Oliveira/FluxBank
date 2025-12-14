package com.example.fluxbank.network.models

import com.google.gson.annotations.SerializedName

data class ContatoRecente(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("numeroConta")
    val numeroConta: String? = null,

    @SerializedName("agencia")
    val agencia: String? = null,

    @SerializedName("nomeUsuario")
    val nome: String? = null,

    @SerializedName("cpf")
    val cpf: String? = null,

    @SerializedName("cnpj")
    val cnpj: String? = null,

    @SerializedName("chavePix")
    val chavePix: String = "",

    @SerializedName("instituicao")
    val instituicao: String = "FluxBank",

    val documento: String? = cpf ?: cnpj,

    val documentoMascarado: String? = when {
        cpf != null && cpf.length == 11 ->
            "${cpf.substring(0, 3)}.${cpf.substring(3, 6)}.${cpf.substring(6, 9)}-${cpf.substring(9, 11)}"
        cnpj != null && cnpj.length == 14 ->
            "${cnpj.substring(0, 2)}.${cnpj.substring(2, 5)}.${cnpj.substring(5, 8)}/${cnpj.substring(8, 12)}-${cnpj.substring(12, 14)}"
        else -> "***.***.***-**"
    },

    val tipoDocumento: String? = when {
        cpf != null -> "CPF"
        cnpj != null -> "CNPJ"
        else -> "CPF"
    }
) {
    fun getNomeExibivel(): String {
        return nome?.takeIf { it.isNotBlank() } ?: "Destinatário"
    }

    fun getPrimeiroNome(): String {
        return getNomeExibivel().split(" ").firstOrNull() ?: "Destinatário"
    }
}