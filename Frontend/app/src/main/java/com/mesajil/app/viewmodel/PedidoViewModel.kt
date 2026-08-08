package com.mesajil.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mesajil.app.models.request.FinalizarCompraRequest
import com.mesajil.app.models.response.ErrorResponse
import com.mesajil.app.models.response.PedidoDetalleResponse
import com.mesajil.app.models.response.PedidoFinalizadoResponse
import com.mesajil.app.models.response.PedidoResponse
import com.mesajil.app.repository.PedidoRepository
import kotlinx.coroutines.launch

class PedidoViewModel : ViewModel() {
    private val repository = PedidoRepository()
    private val _resultado = MutableLiveData<Result<PedidoFinalizadoResponse>>()
    private val _pedidos = MutableLiveData<List<PedidoResponse>>()

    val resultado: LiveData<Result<PedidoFinalizadoResponse>> = _resultado
    val pedidos: LiveData<List<PedidoResponse>> = _pedidos

    fun finalizarCompra(request: FinalizarCompraRequest) {
        viewModelScope.launch {
            try {
                val response = repository.finalizarCompra(request)
                if (response.isSuccessful && response.body() != null) {
                    _resultado.value = Result.success(response.body()!!)
                } else {
                    val error = response.errorBody()?.string()
                    val mensaje = try {
                        Gson().fromJson(
                            error,
                            ErrorResponse::class.java
                        ).mensaje
                    } catch (e: Exception) {
                        "No fue posible finalizar la compra."
                    }
                    _resultado.value = Result.failure(Exception(mensaje))
                }
            } catch (e: Exception) {
                _resultado.value = Result.failure(e)
            }
        }
    }

    fun obtenerMisPedidos() {
        viewModelScope.launch {
            try {
                val response = repository.obtenerMisPedidos()
                if (response.isSuccessful) {
                    _pedidos.value = response.body() ?: emptyList()
                } else {
                    _pedidos.value = emptyList()
                }
            } catch (e: Exception) {
                _pedidos.value = emptyList()
            }
        }
    }

    fun obtenerDetallePedido(
        idPedido: Int,
        onSuccess: (PedidoDetalleResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.obtenerDetallePedido(idPedido)

                if (response.isSuccessful) {
                    val pedido = response.body()

                    if (pedido != null) {
                        onSuccess(pedido)
                    } else {
                        onError("No se encontró el detalle del pedido.")
                    }
                } else {
                    val error = response.errorBody()?.string()

                    onError(
                        "Error ${response.code()}: ${error ?: "No se pudo obtener el detalle del pedido."}"
                    )
                }
            } catch (e: Exception) {
                onError(
                    e.message ?: "Error de conexión."
                )
            }
        }
    }

    fun cancelarPedido(
        idPedido: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.cancelarPedido(idPedido)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("No se pudo cancelar el pedido.")
                }
            } catch (e: Exception) {
                onError(
                    e.message ?: "Error de conexión."
                )
            }
        }
    }
}