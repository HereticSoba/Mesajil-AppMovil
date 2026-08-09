package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import com.mesajil.app.mappers.ProductoMapper
import com.mesajil.app.models.Producto
import com.mesajil.app.models.request.ProductoCreateRequest
import com.mesajil.app.models.request.ProductoUpdateRequest
import com.mesajil.app.models.response.ProductoResponse
import retrofit2.Response

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

    suspend fun crearProducto(request: ProductoCreateRequest): Response<ProductoResponse> {
        return ApiClient.productoService.crearProducto(request)
    }

    suspend fun actualizarProducto(
        idProducto: Int,
        request: ProductoUpdateRequest
    ): Response<Unit> {
        return ApiClient.productoService.actualizarProducto(idProducto, request)
    }

    suspend fun obtenerProductosResponse(): List<ProductoResponse> {
        val response = ApiClient.productoService.obtenerProductos()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    suspend fun obtenerProductoPorId(
        idProducto: Int
    ): Response<ProductoResponse>{
        return ApiClient.productoService.obtenerProductoPorId(idProducto)
    }
}