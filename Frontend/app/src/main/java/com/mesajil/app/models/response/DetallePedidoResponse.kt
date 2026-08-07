package com.mesajil.app.models.response

data class DetallePedidoResponse (
    val producto: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double
)