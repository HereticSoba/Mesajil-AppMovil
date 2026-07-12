package com.mesajil.app.models.response

data class LoginResponse (
    val token: String,
    val idUsuario: Int,
    val nombres: String,
    val correo: String,
    val idRol: Int,
)