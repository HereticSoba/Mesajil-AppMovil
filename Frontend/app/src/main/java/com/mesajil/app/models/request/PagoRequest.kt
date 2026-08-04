package com.mesajil.app.models.request

data class PagoRequest (
    val email: String,
    val token: String,
    val metodoPago: String,
    val tipoMetodoPago: String,
    val cuotas: Int,
    val tipoDocumento: String,
    val numeroDocumento: String,
    val tipoEntrega: String,
    val direccionEntrega: String?
)