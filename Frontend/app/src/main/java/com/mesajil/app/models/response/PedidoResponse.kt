package com.mesajil.app.models.response

data class PedidoResponse(
    val idPedido: Int,
    val idUsuario: Int,
    val fechaPedido: String,
    val total: Double,
    val estadoPedido: String
)