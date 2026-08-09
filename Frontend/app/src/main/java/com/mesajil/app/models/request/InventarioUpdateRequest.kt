package com.mesajil.app.models.request

data class InventarioUpdateRequest(
    val idInventario: Int,
    val idProducto: Int,
    val stockActual: Int,
    val stockMinimo: Int
)
