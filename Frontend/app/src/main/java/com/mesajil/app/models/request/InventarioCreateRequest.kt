package com.mesajil.app.models.request

data class InventarioCreateRequest(
    val idProducto: Int,
    val stockActual: Int,
    val stockMinimo: Int
)
