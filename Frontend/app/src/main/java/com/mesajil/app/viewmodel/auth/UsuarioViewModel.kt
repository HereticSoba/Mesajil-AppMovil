package com.mesajil.app.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.mesajil.app.repository.UsuarioRepository
import retrofit2.Response
import com.google.gson.Gson
import com.mesajil.app.models.request.ActualizarPerfilRequest
import com.mesajil.app.models.response.ErrorResponse
import com.mesajil.app.models.response.UsuarioResponse

class UsuarioViewModel : ViewModel() {
    private val repository = UsuarioRepository()
    fun desactivarCuenta(
        idUsuario: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response: Response<Unit> = repository.desactivarCuenta(idUsuario)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val mensaje = try {
                        Gson().fromJson(errorBody, ErrorResponse::class.java).mensaje
                    } catch (e: Exception) {
                        "No se pudo desactivar la cuenta."
                    }
                    onError(mensaje)
                }
            } catch (e: Exception) {
                onError(e.message ?: "No se pudo desactivar la cuenta.")
            }
        }
    }

    fun actualizarPerfil(
        request: ActualizarPerfilRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.actualizarPerfil(request)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val mensaje = try {
                        Gson().fromJson(
                            errorBody,
                            ErrorResponse::class.java
                        ).mensaje
                    } catch (e: Exception) {
                        "No se pudo actualizar la información."
                    }
                    onError(mensaje)
                }
            } catch (e: Exception) {
                onError(e.message ?: "No se pudo actualizar la información.")
            }
        }
    }

    fun obtenerMiPerfil(
        onSuccess: (UsuarioResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.obtenerMiPerfil()
                if (response.isSuccessful) {
                    val usuario = response.body()
                    if (usuario != null) {
                        onSuccess(usuario)
                    } else {
                        onError("No se encontró la información del usuario.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val mensaje = try {
                        Gson().fromJson(
                            errorBody,
                            ErrorResponse::class.java
                        ).mensaje
                    } catch (e: Exception) {
                        "No se pudo obtener la información del usuario."
                    }
                    onError(mensaje)
                }
            }catch (e:Exception){
                onError(
                    e.message
                        ?: "No se pudo obtener la información del usuario."
                )
            }
        }
    }
}