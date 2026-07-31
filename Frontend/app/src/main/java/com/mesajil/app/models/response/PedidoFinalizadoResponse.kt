package com.mesajil.app.models.response

data class PedidoFinalizadoResponse(
    val idPedido: Int,
    val total: Double,
    val mensaje: String
)