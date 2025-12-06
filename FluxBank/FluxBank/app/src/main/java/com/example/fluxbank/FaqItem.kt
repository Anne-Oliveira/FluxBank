package com.example.fluxbank

data class FaqItem(
    val pergunta: String,
    val resposta: String,
    var expandido: Boolean = false
)