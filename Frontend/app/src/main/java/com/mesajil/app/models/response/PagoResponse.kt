package com.mesajil.app.models.response

data class PagoResponse (
    val idOrden: String?,
    val estado: String,
    val detalleEstado: String?,
    val monto: Double,
    val mensaje: String
)