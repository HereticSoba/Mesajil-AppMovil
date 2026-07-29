package com.mesajil.app.api.services

import com.mesajil.app.models.request.DetalleCarritoRequest
import com.mesajil.app.models.response.DetalleCarritoResponse
import retrofit2.Response
import retrofit2.http.*

interface DetalleCarritoService {
    @GET("api/DetalleCarrito")
    suspend fun obtenerTodos(): Response<List<DetalleCarritoResponse>>

    @GET("api/DetalleCarrito/{id}")
    suspend fun obtenerPorId(
        @Path("id") id: Int
    ): Response<DetalleCarritoResponse>

    @POST("api/DetalleCarrito")
    suspend fun crear(
        @Body request: DetalleCarritoRequest
    ): Response<DetalleCarritoResponse>

    @PUT("api/DetalleCarrito/{id}")
    suspend fun actualizar(
        @Path("id") id: Int,
        @Body request: DetalleCarritoRequest
    ): Response<Unit>

    @GET("api/DetalleCarrito/carrito/{idCarrito}")
    suspend fun obtenerPorCarrito(
        @Path("idCarrito") idCarrito: Int
    ): Response<List<DetalleCarritoResponse>>

    @DELETE("api/DetalleCarrito/{id}")
    suspend fun eliminar(
        @Path("id") id: Int
    ): Response<Unit>
}