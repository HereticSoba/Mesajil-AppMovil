package com.mesajil.app.models.request

data class CategoriaUpdateRequest(
    val idCategoria: Int,
    val nombre: String,
    val descripcion: String?
)
