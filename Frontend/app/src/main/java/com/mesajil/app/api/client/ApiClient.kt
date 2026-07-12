package com.mesajil.app.api.client

import com.mesajil.app.api.services.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL="http://192.168.100.54:5228/"

    val retrofit: Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }
}