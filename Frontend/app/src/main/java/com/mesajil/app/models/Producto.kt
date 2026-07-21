package com.mesajil.app.models

data class Producto (
    val idProducto: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val idCategoria: Int
)