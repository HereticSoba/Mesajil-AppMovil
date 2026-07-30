package com.mesajil.app.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesajil.app.models.response.RegistroResponse
import com.mesajil.app.repository.AuthRepository
import kotlinx.coroutines.launch
import com.google.gson.Gson
import com.mesajil.app.models.response.ErrorResponse

class RegistroViewModel : ViewModel() {
    private val repository = AuthRepository()
    private val _registroResult = MutableLiveData<Result<RegistroResponse>>()
    val registroResult: LiveData<Result<RegistroResponse>> = _registroResult

    fun registrar(
        nombres: String,
        apellidos: String,
        correo: String,
        contrasena: String,
        telefono: String?,
        direccion: String?
    ) {
        viewModelScope.launch {
            try {
                val response = repository.registrar(
                    nombres,
                    apellidos,
                    correo,
                    contrasena,
                    telefono,
                    direccion
                )
                if (response.isSuccessful && response.body() != null) {
                    _registroResult.value = Result.success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val mensaje = try {
                        Gson().fromJson(errorBody, ErrorResponse::class.java)?.mensaje?: "Ocurrió un error."
                    }catch (e:Exception){
                        "Ocurrió un error."
                    }
                    _registroResult.value = Result.failure(Exception(mensaje))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _registroResult.value = Result.failure(e)
            }
        }
    }
}