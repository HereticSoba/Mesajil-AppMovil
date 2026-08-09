package com.mesajil.app.models.request

data class UsuarioUpdateRequest(
    val idUsuario: Int,
    val idRol: Int,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val telefono: String?,
    val direccion: String?,
    val estado: Boolean
)
