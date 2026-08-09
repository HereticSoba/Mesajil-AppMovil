package com.mesajil.app.models.response

data class ProductoMayorDemandaResponse(
    val idProducto: Int,
    val nombre: String,
    val cantidadVendida: Int
)
