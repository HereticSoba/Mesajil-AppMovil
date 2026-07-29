package com.mesajil.app.models

data class Producto (
    val idProducto: Int,
    val idCategoria: Int,
    val nombre: String,
    val descripcion: String?,
    val marca: String,
    val modelo: String,
    val precio: Double,
    val estado: Boolean,
    val fechaRegistro: String
)