package com.mesajil.app.api.services

import com.mesajil.app.models.request.ProductoCreateRequest
import com.mesajil.app.models.request.ProductoUpdateRequest
import com.mesajil.app.models.response.ProductoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductoService {
    @GET("api/Producto")
    suspend fun obtenerProductos(): Response<List<ProductoResponse>>

    @GET("api/Producto/{id}")
    suspend fun obtenerProductoPorId(
        @Path("id") idProducto: Int
    ): Response<ProductoResponse>

    @POST("api/Producto")
    suspend fun crearProducto(@Body request: ProductoCreateRequest): Response<ProductoResponse>

    @PUT("api/Producto/{id}")
    suspend fun actualizarProducto(
        @Path("id") idProducto: Int,
        @Body request: ProductoUpdateRequest
    ): Response<Unit>
}