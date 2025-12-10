package com.example.fluxbank

sealed class ComprovanteItem {
    data class Data(val data: String) : ComprovanteItem()
    data class Transacao(val tipo: String, val nome: String, val valor: String) : ComprovanteItem()
}