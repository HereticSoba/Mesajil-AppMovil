package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import com.mesajil.app.mappers.ProductoMapper
import com.mesajil.app.models.Producto

class ProductoRepository {
    suspend fun obtenerProductos(): List<Producto> {
        val response = ApiClient.productoService.obtenerProductos()
        if (response.isSuccessful) {
            return response.body()
                ?.map { productoResponse -> ProductoMapper.toModel(productoResponse) }
                ?: emptyList()
        }
        return emptyList()
    }
}