package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import com.mesajil.app.models.response.CategoriaResponse

class CategoriaRepository {
    suspend fun obtenerCategorias(): List<CategoriaResponse> {
        val response = ApiClient.categoriaService.obtenerCategorias()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }
}