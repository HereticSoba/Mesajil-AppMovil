package com.mesajil.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesajil.app.models.request.ProductoCreateRequest
import com.mesajil.app.models.request.ProductoUpdateRequest
import com.mesajil.app.models.response.ProductoResponse
import com.mesajil.app.repository.ProductoRepository
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {
    private val repository = ProductoRepository()
    fun crearProducto(
        request: ProductoCreateRequest,
        onSuccess: (ProductoResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.crearProducto(request)
                if (response.isSuccessful) {
                    val producto = response.body()
                    if (producto != null) {
                        onSuccess(producto)
                    } else {
                        onError("No se recibió el producto creado.")
                    }
                } else {
                    onError("No se pudo registrar el producto.")
                }
            } catch (e: Exception) {
                onError(
                    e.message ?: "Error de conexión."
                )
            }
        }
    }

    fun actualizarProducto(
        idProducto: Int,
        request: ProductoUpdateRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.actualizarProducto(idProducto, request)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val error = response.errorBody()?.string()
                    onError("Error ${response.code()}: ${error ?: "Sin detalle"}"
                    )
                }
            } catch (e: Exception) {
                onError(e.message ?: "Error de conexión.")
            }
        }
    }

    fun obtenerProductosAdmin(
        onSuccess: (List<ProductoResponse>) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val productos = repository.obtenerProductosResponse()
                if (productos.isNotEmpty()) {
                    onSuccess(productos)
                } else {
                    onError("No se encontraron productos.")
                }
            } catch (e: Exception) {
                onError(
                    e.message ?: "Error de conexión."
                )
            }
        }
    }

    fun obtenerProductoPorId(
        idProducto: Int,
        onSuccess: (ProductoResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.obtenerProductoPorId(idProducto)
                if (response.isSuccessful) {
                    val producto = response.body()
                    if (producto != null) {
                        onSuccess(producto)
                    } else {
                        onError("No se encontró el producto.")
                    }
                } else {
                    onError("No se pudo obtener el producto.")
                }
            } catch (e: Exception) {
                onError(
                    e.message ?: "Error de conexión."
                )
            }
        }
    }
}