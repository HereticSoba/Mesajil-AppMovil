package com.mesajil.app.api.services

import com.mesajil.app.models.request.LoginRequest
import com.mesajil.app.models.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}