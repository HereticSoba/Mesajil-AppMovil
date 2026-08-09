package com.mesajil.app.models.request

data class ProductoCreateRequest (
    val idCategoria: Int,
    val nombre: String,
    val descripcion: String?,
    val marca: String,
    val modelo: String,
    val precio: Double
)