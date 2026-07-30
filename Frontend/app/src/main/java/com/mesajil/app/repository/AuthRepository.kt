package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import com.mesajil.app.models.request.LoginRequest
import com.mesajil.app.models.response.LoginResponse
import com.mesajil.app.models.request.UsuarioRegistroRequest
import com.mesajil.app.models.response.RegistroResponse
import retrofit2.Response

class AuthRepository {
    suspend fun login(
        correo: String,
        contrasena: String
    ): Response<LoginResponse> {
        val request = LoginRequest(correo, contrasena)
        return ApiClient.authService.login(request)
    }

    suspend fun registrar(
        nombres: String,
        apellidos: String,
        correo: String,
        contrasena: String,
        telefono: String?,
        direccion: String?
    ): Response<RegistroResponse> {
        val request =
            UsuarioRegistroRequest(nombres, apellidos, correo, contrasena, telefono, direccion)
        return ApiClient.authService.registrar(request)
    }
}