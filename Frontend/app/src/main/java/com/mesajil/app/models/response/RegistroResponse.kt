package com.mesajil.app.models.response

data class RegistroResponse(
    val idUsuario: Int,
    val nombres: String,
    val correo: String,
    val mensaje: String
)