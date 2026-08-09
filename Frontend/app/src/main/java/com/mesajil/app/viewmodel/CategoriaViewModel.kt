package com.mesajil.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesajil.app.models.request.CategoriaCreateRequest
import com.mesajil.app.models.request.CategoriaUpdateRequest
import com.mesajil.app.models.response.CategoriaResponse
import com.mesajil.app.repository.CategoriaRepository
import kotlinx.coroutines.launch

class CategoriaViewModel : ViewModel() {
    private val repository = CategoriaRepository()

    fun obtenerCategorias(
        onSuccess: (List<CategoriaResponse>) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val categorias = repository.obtenerCategorias()
                if (categorias.isNotEmpty()) {
                    onSuccess(categorias)
                } else {
                    onError("No se encontraron categorías.")
                }
            } catch (e: Exception) {
                onError(
                    e.message ?: "Error de conexión."
                )
            }
        }
    }

    fun crearCategoria(
        request: CategoriaCreateRequest,
        onSuccess: (CategoriaResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.crearCategoria(request)
                if (response.isSuccessful) {
                    val categoria = response.body()
                    if (categoria != null) {
                        onSuccess(categoria)
                    } else {
                        onError(
                            "No se recibió la categoría creada."
                        )
                    }
                } else {
                    onError(
                        "No se pudo registrar la categoría."
                    )
                }
            } catch (e: Exception) {
                onError(
                    e.message ?: "Error de conexión."
                )
            }
        }
    }

    fun actualizarCategoria(
        idCategoria: Int,
        request: CategoriaUpdateRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response =
                    repository.actualizarCategoria(
                        idCategoria,
                        request
                    )
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(
                        "No se pudo actualizar la categoría."
                    )
                }
            } catch (e: Exception) {
                onError(
                    e.message ?: "Error de conexión."
                )
            }
        }
    }

    fun eliminarCategoria(
        idCategoria: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response =
                    repository.eliminarCategoria(
                        idCategoria
                    )
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(
                        "No se pudo eliminar la categoría."
                    )
                }
            } catch (e: Exception) {
                onError(
                    e.message ?: "Error de conexión."
                )
            }
        }
    }
}