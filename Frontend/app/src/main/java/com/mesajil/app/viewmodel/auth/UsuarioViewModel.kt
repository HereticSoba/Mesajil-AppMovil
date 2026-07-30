package com.mesajil.app.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.mesajil.app.repository.UsuarioRepository
import retrofit2.Response
import com.google.gson.Gson
import com.mesajil.app.models.response.ErrorResponse

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
            }catch (e: Exception) {
                onError(e.message?: "No se pudo desactivar la cuenta.")
            }
        }
    }
}