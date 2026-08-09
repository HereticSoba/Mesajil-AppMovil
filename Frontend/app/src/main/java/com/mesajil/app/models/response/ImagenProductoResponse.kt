package com.mesajil.app.models.response

data class ImagenProductoResponse(
    val idImagen: Int,
    val idProducto: Int,
    val urlImagen: String,
    val principal: Boolean
)