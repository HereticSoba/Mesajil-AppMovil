package com.mesajil.app.api.services

import com.mesajil.app.models.request.CategoriaCreateRequest
import com.mesajil.app.models.request.CategoriaUpdateRequest
import com.mesajil.app.models.response.CategoriaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CategoriaService {
    @GET("api/Categoria")
    suspend fun obtenerCategorias(): Response<List<CategoriaResponse>>

    @GET("api/Categoria/{id}")
    suspend fun obtenerCategoriaPorId(
        @Path("id") idCategoria: Int
    ): Response<CategoriaResponse>

    @POST("api/Categoria")
    suspend fun crearCategoria(
        @Body request: CategoriaCreateRequest
    ): Response<CategoriaResponse>

    @PUT("api/Categoria/{id}")
    suspend fun actualizarCategoria(
        @Path("id") idCategoria: Int, @Body request: CategoriaUpdateRequest
    ): Response<Unit>

    @DELETE("api/Categoria/{id}")
    suspend fun eliminarCategoria(
        @Path("id") idCategoria: Int
    ): Response<Unit>
}