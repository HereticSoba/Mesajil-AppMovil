package com.mesajil.app.api.client

import com.mesajil.app.api.services.AuthService
import com.mesajil.app.api.services.ProductoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.mesajil.app.api.services.CarritoService

object ApiClient {
    private const val BASE_URL = "http://192.168.100.54:5228/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }
    val productoService: ProductoService by lazy {
        retrofit.create(ProductoService::class.java)
    }
    val carritoService: CarritoService by lazy {
        retrofit.create(CarritoService::class.java)
    }
}