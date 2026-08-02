package com.mesajil.app.models.response

data class CategoriaResponse(
    val idCategoria: Int,
    val nombre: String,
    val descripcion: String?,
    val fechaRegistro: String,
    val estado: Boolean
)
