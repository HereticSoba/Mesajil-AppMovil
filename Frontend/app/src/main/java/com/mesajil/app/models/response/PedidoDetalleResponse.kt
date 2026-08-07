package com.mesajil.app.models.response

data class PedidoDetalleResponse (
    val idPedido: Int,
    val fechaPedido: String,
    val total: Double,
    val estadoPedido: String,
    val estadoPago: String,
    val tipoEntrega: String,
    val direccionEntrega: String,
    val tiendaRecojo: String?,
    val costoEnvio: Double,
    val idOrdenMercadoPago: String?,
    val productos: List<DetallePedidoResponse>
)