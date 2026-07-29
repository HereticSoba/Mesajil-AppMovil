package com.mesajil.app.api.services

import com.mesajil.app.models.response.ProductoResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProductoService {
    @GET("api/Producto")
    suspend fun obtenerProductos(): Response<List<ProductoResponse>>
}