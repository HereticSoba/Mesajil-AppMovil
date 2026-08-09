package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import com.mesajil.app.models.request.ActualizarPerfilRequest
import com.mesajil.app.models.request.UsuarioCreateRequest
import com.mesajil.app.models.request.UsuarioUpdateRequest
import com.mesajil.app.models.response.UsuarioResponse
import retrofit2.Response

class UsuarioRepository {
    private val usuarioService = ApiClient.usuarioService
    suspend fun desactivarCuenta(
        idUsuario: Int
    ): Response<Unit> {
        return usuarioService.desactivarCuenta(idUsuario)
    }

    suspend fun actualizarPerfil(
        request: ActualizarPerfilRequest
    ): Response<Unit> {
        return usuarioService.actualizarPerfil(request)
    }

    suspend fun obtenerMiPerfil(): Response<UsuarioResponse> {
        return usuarioService.obtenerMiPerfil()
    }

    suspend fun obtenerUsuarios():
            Response<List<UsuarioResponse>> {

        return usuarioService.obtenerUsuarios()
    }

    suspend fun crearUsuario(
        request: UsuarioCreateRequest
    ): Response<UsuarioResponse> {
        return usuarioService.crearUsuario(request)
    }

    suspend fun actualizarUsuario(
        idUsuario: Int, request: UsuarioUpdateRequest
    ): Response<Unit> {
        return usuarioService.actualizarUsuario(idUsuario, request)
    }
}