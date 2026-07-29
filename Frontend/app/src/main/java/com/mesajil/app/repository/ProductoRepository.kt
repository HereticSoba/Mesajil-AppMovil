package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import com.mesajil.app.models.Producto
import retrofit2.Response

class ProductoRepository {
    suspend fun obtenerProductos(): Response<List<Producto>>{
        return ApiClient.productoService.obtenerProductos()
    }
}