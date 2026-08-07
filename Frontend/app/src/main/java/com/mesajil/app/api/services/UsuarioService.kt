package com.mesajil.app.api.services

import com.mesajil.app.models.request.ActualizarPerfilRequest
import com.mesajil.app.models.response.UsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuarioService {
    @GET("api/Usuario/mi-perfil")
    suspend fun obtenerMiPerfil(): Response<UsuarioResponse>

    @DELETE("api/Usuario/{id}")
    suspend fun desactivarCuenta(
        @Path("id") idUsuario: Int
    ): Response<Unit>

    @PUT("api/Usuario/mi-perfil")
    suspend fun actualizarPerfil(
        @Body request: ActualizarPerfilRequest
    ): Response<Unit>
}