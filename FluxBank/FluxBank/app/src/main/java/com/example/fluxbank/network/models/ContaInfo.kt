package com.example.fluxbank.network.models

import com.google.gson.annotations.SerializedName

data class ContaInfo(
    @SerializedName("id")
    val id: Long,

    @SerializedName("numeroConta")
    val numeroConta: String,

    @SerializedName("agencia")
    val agencia: String,

    @SerializedName("nomeUsuario")
    val nomeUsuario: String,

    @SerializedName("cpf")
    val cpf: String? = null,

    @SerializedName("cnpj")
    val cnpj: String? = null,

    @SerializedName("chavePix")
    val chavePix: String,

    @SerializedName("instituicao")
    val instituicao: String = "FluxBank"
) {
    fun getDocumento(): String {
        return cpf ?: cnpj ?: ""
    }

    fun getDocumentoMascarado(): String {
        return when {
            cpf != null && cpf.length == 11 -> {
                "***.***.${cpf.substring(6, 9)}-**"
            }
            cnpj != null && cnpj.length == 14 -> {
                "**.***.***: ***/${cnpj.substring(8, 12)}-**"
            }
            else -> "***.***.***-**"
        }
    }

    fun getTipoDocumento(): String {
        return when {
            cpf != null -> "CPF"
            cnpj != null -> "CNPJ"
            else -> "CPF"
        }
    }

    fun getDocumentoFormatado(): String {
        return when {
            cpf != null && cpf.length == 11 -> {
                "${cpf.substring(0, 3)}.${cpf.substring(3, 6)}.${cpf.substring(6, 9)}-${cpf.substring(9, 11)}"
            }
            cnpj != null && cnpj.length == 14 -> {
                "${cnpj.substring(0, 2)}.${cnpj.substring(2, 5)}.${cnpj.substring(5, 8)}/${cnpj.substring(8, 12)}-${cnpj.substring(12, 14)}"
            }
            else -> getDocumento()
        }
    }
}