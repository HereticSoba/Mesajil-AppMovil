package com.mesajil.app.models.response

data class CarritoResponse (
    val idCarrito: Int,
    val idUsuario: Int,
    val fechaCreacion: String,
    val estado: Boolean
)