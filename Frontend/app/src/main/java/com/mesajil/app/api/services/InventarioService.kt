package com.mesajil.app.api.services

import com.mesajil.app.models.request.InventarioCreateRequest
import com.mesajil.app.models.request.InventarioUpdateRequest
import com.mesajil.app.models.response.InventarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface InventarioService {
    @GET("api/Inventario")
    suspend fun obtenerInventarios():
            Response<List<InventarioResponse>>

    @GET("api/Inventario/{id}")
    suspend fun obtenerInventarioPorId(
        @Path("id") idInventario: Int
    ): Response<InventarioResponse>

    @POST("api/Inventario")
    suspend fun crearInventario(
        @Body request: InventarioCreateRequest
    ): Response<InventarioResponse>

    @PUT("api/Inventario")
    suspend fun actualizarInventario(
        @Body request: InventarioUpdateRequest
    ): Response<Unit>

    @DELETE("api/Inventario/{id}")
    suspend fun eliminarInventario(
        @Path("id") idInventario: Int
    ): Response<Unit>
}