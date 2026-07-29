package com.mesajil.app.api.services

import com.mesajil.app.models.response.CarritoResponse
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.GET

interface CarritoService {
    @GET("api/Carrito/usuario/{idUsuario}")
    suspend fun obtenerOCrearCarrito(
        @Path("idUsuario") idUsuario: Int
    ): Response<CarritoResponse>
}