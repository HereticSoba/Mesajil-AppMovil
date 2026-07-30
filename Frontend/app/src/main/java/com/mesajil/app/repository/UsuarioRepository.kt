package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import retrofit2.Response

class UsuarioRepository {
    private val usuarioService = ApiClient.usuarioService
    suspend fun desactivarCuenta(
        idUsuario: Int
    ): Response<Unit>{
        return usuarioService.desactivarCuenta(idUsuario)
    }
}