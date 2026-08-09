package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import com.mesajil.app.models.request.InventarioCreateRequest
import com.mesajil.app.models.request.InventarioUpdateRequest
import com.mesajil.app.models.response.InventarioResponse
import retrofit2.Response

class InventarioRepository {
    suspend fun obtenerInventarios():
            Response<List<InventarioResponse>> {
        return ApiClient.inventarioService
            .obtenerInventarios()
    }

    suspend fun obtenerInventarioPorId(
        idInventario: Int
    ): Response<InventarioResponse> {
        return ApiClient.inventarioService
            .obtenerInventarioPorId(idInventario)
    }

    suspend fun crearInventario(
        request: InventarioCreateRequest
    ): Response<InventarioResponse> {
        return ApiClient.inventarioService
            .crearInventario(request)
    }

    suspend fun actualizarInventario(
        request: InventarioUpdateRequest
    ): Response<Unit> {
        return ApiClient.inventarioService
            .actualizarInventario(request)
    }

    suspend fun eliminarInventario(
        idInventario: Int
    ): Response<Unit> {
        return ApiClient.inventarioService
            .eliminarInventario(idInventario)
    }
}