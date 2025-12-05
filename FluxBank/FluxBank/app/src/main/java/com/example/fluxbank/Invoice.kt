package com.example.fluxbank

data class Invoice(
    val month: String,
    val value: String,
    val dueDate: String,
    val bestDay: String?,
    val isOpen: Boolean,
    val canAnticipate: Boolean
)