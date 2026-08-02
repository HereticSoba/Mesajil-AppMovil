package com.mesajil.app.models.response

data class DetalleCarritoResponse(
    val idDetalleCarrito: Int,
    val idCarrito: Int,
    val idProducto: Int,
    val nombreProducto: String,
    val marca: String,
    val modelo: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double,
    val stock: Int,
    val urlImagen: String?
)