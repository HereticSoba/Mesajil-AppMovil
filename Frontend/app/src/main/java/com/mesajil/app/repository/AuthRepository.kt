package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import com.mesajil.app.models.request.LoginRequest
import com.mesajil.app.models.response.LoginResponse
import retrofit2.Response

class AuthRepository {
    suspend fun login(
        correo: String,
        contrasena: String
    ): Response<LoginResponse> {
        val request = LoginRequest(correo, contrasena)
        return ApiClient.authService.login(request)
    }
}