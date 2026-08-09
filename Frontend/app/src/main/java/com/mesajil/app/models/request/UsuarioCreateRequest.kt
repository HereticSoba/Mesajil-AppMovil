package com.mesajil.app.models.request

data class UsuarioCreateRequest(
    val idRol: Int,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val contrasena: String,
    val telefono: String?,
    val direccion: String?
)
