package com.mesajil.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesajil.app.models.response.ImagenProductoResponse
import com.mesajil.app.repository.ImagenProductoRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaType

class ImagenProductoViewModel : ViewModel() {
    private val repository = ImagenProductoRepository()
    fun subirImagen(
        idProducto: RequestBody,
        principal: RequestBody,
        imagen: MultipartBody.Part,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response: Response<*> =
                    repository.subirImagen(idProducto, principal, imagen)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("No se pudo subir la imagen.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Error de conexión.")
            }
        }
    }

    fun obtenerImagenProducto(
        idProducto: Int,
        onSuccess: (ImagenProductoResponse?) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.obtenerImagenes()
                if (response.isSuccessful) {
                    val imagen = response.body()
                        ?.firstOrNull {
                            it.idProducto == idProducto
                        }
                    onSuccess(imagen)
                } else {
                    onError("No se pudieron obtener las imagenes.")
                }
            } catch (e: Exception) {
                onError(
                    e.message ?: "Error de conexión."
                )
            }
        }
    }

    fun actualizarImagen(
        idImagen: Int,
        idProducto: Int,
        imagen: MultipartBody.Part,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val idImagenBody = idImagen.toString().toRequestBody("text/plain".toMediaType())
                val idProductoBody = idProducto.toString().toRequestBody("text/plain".toMediaType())
                val principalBody = "true".toRequestBody("text/plain".toMediaType())

                val response = repository.actualizarImagen(
                    idImagen = idImagen,
                    idImagenBody = idImagenBody,
                    idProducto = idProductoBody,
                    principal = principalBody,
                    imagen = imagen
                )
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(
                        "No se pudo reemplazar la imagen." +
                                "Error ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                onError(
                    e.message ?: "Error de conexión."
                )
            }
        }
    }

    fun eliminarImagen(
        idImagen: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response =
                    repository.eliminarImagen(idImagen)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(
                        "No se pudo eliminar la imagen. " +
                                "Error ${response.code()}"
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