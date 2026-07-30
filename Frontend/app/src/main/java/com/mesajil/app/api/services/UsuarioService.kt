package com.mesajil.app.api.services

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Path

interface UsuarioService {
    @DELETE("api/Usuario/{id}")
    suspend fun desactivarCuenta(
        @Path("id") idUsuario: Int
    ): Response<Unit>
}