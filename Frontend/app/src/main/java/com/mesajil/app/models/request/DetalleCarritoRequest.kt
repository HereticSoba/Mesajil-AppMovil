package com.mesajil.app.models.request

data class DetalleCarritoRequest(
    val idCarrito: Int,
    val idProducto: Int,
    val cantidad: Int,
    val precioUnitario: Double
)