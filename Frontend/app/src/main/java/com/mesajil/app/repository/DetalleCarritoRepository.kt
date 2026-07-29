package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import com.mesajil.app.models.request.DetalleCarritoRequest
import com.mesajil.app.models.response.DetalleCarritoResponse
import android.util.Log

class DetalleCarritoRepository {
    suspend fun obtenerTodos(): List<DetalleCarritoResponse>? {
        val response = ApiClient.detalleCarritoService.obtenerTodos()
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    suspend fun obtenerPorId(id: Int): DetalleCarritoResponse? {
        val response = ApiClient.detalleCarritoService.obtenerPorId(id)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    suspend fun crear(request: DetalleCarritoRequest): DetalleCarritoResponse? {
        val response = ApiClient.detalleCarritoService.crear(request)
        Log.d("DetalleCarritoRepository", "HTTP: ${response.code()}")
        Log.d("DetalleCarritoRepository", "Mensaje: ${response.message()}")
        Log.d("DetalleCarritoRepository", "Cuerpo: ${response.errorBody()?.string()}")
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    suspend fun actualizar(
        id: Int,
        request: DetalleCarritoRequest
    ): Boolean {
        val response = ApiClient.detalleCarritoService.actualizar(id, request)
        return response.isSuccessful
    }

    suspend fun obtenerPorCarrito(idCarrito: Int): List<DetalleCarritoResponse>? {
        val response = ApiClient.detalleCarritoService.obtenerPorCarrito(idCarrito)
        Log.d("DetalleCarritoRepository", "HTTP: ${response.code()}")
        Log.d("DetalleCarritoRepository", "Mensaje: ${response.message()}")
        Log.d("DetalleCarritoRepository", "Body: ${response.body()}")
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    suspend fun eliminar(id: Int): Boolean {
        val response = ApiClient.detalleCarritoService.eliminar(id)
        return response.isSuccessful
    }
}