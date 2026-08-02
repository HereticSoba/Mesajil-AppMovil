package com.mesajil.app.models.response

data class ProductoResponse (
    val idProducto: Int,
    val idCategoria: Int,
    val nombre: String,
    val descripcion: String,
    val marca: String,
    val modelo: String,
    val precio: Double,
    val stockActual: Int,
    val urlImagen: String?,
    val estado: Boolean,
    val fechaRegistro: String
)