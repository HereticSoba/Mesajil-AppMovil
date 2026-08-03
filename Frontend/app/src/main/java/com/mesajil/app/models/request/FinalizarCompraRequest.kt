package com.mesajil.app.models.request

data class FinalizarCompraRequest(
    val tipoEntrega: String,
    val direccionEntrega: String?
)
