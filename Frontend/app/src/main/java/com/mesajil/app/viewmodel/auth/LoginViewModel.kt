package com.mesajil.app.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesajil.app.models.response.LoginResponse
import com.mesajil.app.repository.AuthRepository
import kotlinx.coroutines.launch

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
                _loginResult.value = Result.failure(Exception("HTTP ${response.code()}\n$error"))
            }
        }catch (e: Exception){
            e.printStackTrace()
            _loginResult.value=Result.failure(e)
            }
        }
    }
}