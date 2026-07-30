package com.mesajil.app.api.services

import com.mesajil.app.models.request.LoginRequest
import com.mesajil.app.models.response.LoginResponse
import com.mesajil.app.models.request.UsuarioRegistroRequest
import com.mesajil.app.models.response.RegistroResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("api/auth/registro")
    suspend fun registrar(
        @Body request: UsuarioRegistroRequest
    ): Response<RegistroResponse>
}