package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import com.mesajil.app.models.request.CategoriaCreateRequest
import com.mesajil.app.models.request.CategoriaUpdateRequest
import com.mesajil.app.models.response.CategoriaResponse
import retrofit2.Response

class CategoriaRepository {
    suspend fun obtenerCategorias(): List<CategoriaResponse> {
        val response = ApiClient.categoriaService.obtenerCategorias()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    suspend fun obtenerCategoriaPorId(
        idCategoria: Int
    ): Response<CategoriaResponse> {

        return ApiClient.categoriaService
            .obtenerCategoriaPorId(idCategoria)
    }

    suspend fun crearCategoria(
        request: CategoriaCreateRequest
    ): Response<CategoriaResponse> {

        return ApiClient.categoriaService
            .crearCategoria(request)
    }

    suspend fun actualizarCategoria(
        idCategoria: Int,
        request: CategoriaUpdateRequest
    ): Response<Unit> {

        return ApiClient.categoriaService
            .actualizarCategoria(
                idCategoria,
                request
            )
    }

    suspend fun eliminarCategoria(
        idCategoria: Int
    ): Response<Unit> {

        return ApiClient.categoriaService
            .eliminarCategoria(idCategoria)
    }
}