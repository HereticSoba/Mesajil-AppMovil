package com.mesajil.app.models.request

data  class ActualizarPerfilRequest(
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val telefono: String?,
    val direccion: String?
)