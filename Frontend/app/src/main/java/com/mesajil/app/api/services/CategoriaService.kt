package com.mesajil.app.api.services

import com.mesajil.app.models.response.CategoriaResponse
import retrofit2.Response
import retrofit2.http.GET

interface CategoriaService {
    @GET("api/Categoria")
    suspend fun obtenerCategorias(): Response<List<CategoriaResponse>>
}