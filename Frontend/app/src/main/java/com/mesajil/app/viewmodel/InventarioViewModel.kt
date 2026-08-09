package com.mesajil.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesajil.app.models.request.InventarioCreateRequest
import com.mesajil.app.models.request.InventarioUpdateRequest
import com.mesajil.app.models.response.InventarioResponse
import com.mesajil.app.repository.InventarioRepository
import kotlinx.coroutines.launch

class InventarioViewModel : ViewModel() {
    private val repository = InventarioRepository()

    fun obtenerInventarios(
        onSuccess: (List<InventarioResponse>) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.obtenerInventarios()
                if (response.isSuccessful) {
                    onSuccess(response.body() ?: emptyList())
                } else {
                    onError("No se pudo obtener el inventario.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Error de conexión.")
            }
        }
    }

    fun obtenerInventarioPorId(
        idInventario: Int,
        onSuccess: (InventarioResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.obtenerInventarioPorId(idInventario)

                if (response.isSuccessful) {
                    val inventario = response.body()
                    if (inventario != null) {
                        onSuccess(inventario)
                    } else {
                        onError("No se encontró el inventario.")
                    }
                } else {
                    onError("No se pudo obtener el inventario.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Error de conexión.")
            }
        }
    }

    fun crearInventario(
        request: InventarioCreateRequest,
        onSuccess: (InventarioResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response =
                    repository.crearInventario(request)
                if (response.isSuccessful) {
                    val inventario = response.body()
                    if (inventario != null) {
                        onSuccess(inventario)
                    } else {
                        onError("No se recibió el inventario creado.")
                    }
                } else {
                    onError("No se pudo registrar el inventario.")
                }

            } catch (e: Exception) {
                onError(e.message ?: "Error de conexión.")
            }
        }
    }

    fun actualizarInventario(
        request: InventarioUpdateRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.actualizarInventario(request)

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("No se pudo actualizar el inventario.")
                }

            } catch (e: Exception) {
                onError(e.message ?: "Error de conexión.")
            }
        }
    }

    fun eliminarInventario(
        idInventario: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response =
                    repository.eliminarInventario(idInventario)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("No se pudo eliminar el inventario.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Error de conexión.")
            }
        }
    }
}