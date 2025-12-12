package com.example.fluxbank.network.models

data class ContaInfo(
    val id: Long,
    val numeroConta: String,
    val agencia: String,
    val nomeUsuario: String,
    val cpf: String?,
    val cnpj: String?,
    val chavePix: String,
    val instituicao: String = "FluxBank"
) {
    fun getDocumento(): String {
        return cpf ?: cnpj ?: ""
    }

    fun getDocumentoMascarado(): String {
        return when {
            cpf != null && cpf.length == 11 -> {
                "***.${cpf.substring(3, 6)}.${cpf.substring(6, 9)}-**"
            }
            cnpj != null && cnpj.length == 14 -> {
                "**.${cnpj.substring(2, 5)}.${cnpj.substring(5, 8)}/****-**"
            }
            else -> "***.***.***: ***-**"
        }
    }

    fun getTipoDocumento(): String {
        return if (cpf != null) "CPF" else "CNPJ"
    }
}