package com.example.fluxbank

data class CardInfo(
    val type: String,
    val number: String,
    val imageRes: Int,
    val gradientStart: String,
    val gradientCenter: String,
    val gradientEnd: String,
    var isBlocked: Boolean = false
)