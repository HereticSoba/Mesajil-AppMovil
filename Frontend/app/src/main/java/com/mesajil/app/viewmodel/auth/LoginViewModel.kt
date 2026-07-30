package com.mesajil.app.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesajil.app.models.response.LoginResponse
import com.mesajil.app.repository.AuthRepository
import kotlinx.coroutines.launch
import com.google.gson.Gson
import com.mesajil.app.models.response.ErrorResponse

class LoginViewModel: ViewModel() {
    private val repository = AuthRepository()
    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult:LiveData<Result<LoginResponse>> = _loginResult
    fun login(correo: String, contrasena: String){
        viewModelScope.launch {
        try{
            val response = repository.login(correo, contrasena)
            if (response.isSuccessful && response.body() != null){
                _loginResult.value = Result.success(response.body()!!)
            }else{
                val error = response.errorBody()?.string()
                val mensaje = try{
                    Gson().fromJson(error, ErrorResponse::class.java)?.mensaje ?: "Ocurrió un error."
                }catch (e: Exception){
                    "Ocurrió un error."
                }
                _loginResult.value = Result.failure(Exception(mensaje))
            }
        }catch (e: Exception){
            e.printStackTrace()
            _loginResult.value=Result.failure(e)
            }
        }
    }
}