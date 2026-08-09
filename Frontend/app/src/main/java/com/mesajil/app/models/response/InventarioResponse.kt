package com.mesajil.app.models.response

data class InventarioResponse(
    val idInventario: Int,
    val idProducto: Int,
    val stockActual: Int,
    val stockMinimo: Int,
    val ulitmaActualizacion: String
)