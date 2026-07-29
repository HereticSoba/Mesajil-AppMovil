package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import com.mesajil.app.models.response.CarritoResponse

class CarritoRepository {
    suspend fun obtenerOCrearCarrito(idUsuario: Int): CarritoResponse? {
        val response = ApiClient.carritoService.obtenerOCrearCarrito(idUsuario)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }
}