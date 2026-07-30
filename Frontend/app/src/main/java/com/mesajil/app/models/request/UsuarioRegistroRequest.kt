package com.mesajil.app.models.request

data class UsuarioRegistroRequest(
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val contrasena: String,
    val telefono: String?,
    val direccion: String?
)