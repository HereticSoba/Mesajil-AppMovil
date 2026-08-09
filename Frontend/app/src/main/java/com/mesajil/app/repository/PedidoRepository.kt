package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import com.mesajil.app.api.client.ApiClient.pedidoService
import com.mesajil.app.models.request.FinalizarCompraRequest
import com.mesajil.app.models.response.PedidoDetalleResponse
import com.mesajil.app.models.response.ProductoMayorDemandaResponse
import retrofit2.Response

class PedidoRepository {
    suspend fun finalizarCompra(request: FinalizarCompraRequest) =
        ApiClient.pedidoService.finalizarCompra(request)

    suspend fun obtenerMisPedidos() =
        ApiClient.pedidoService.obtenerMisPedidos()

    suspend fun obtenerDetallePedido(idPedido: Int): Response<PedidoDetalleResponse> {
        return pedidoService.obtenerDetallePedido(idPedido)
    }

    suspend fun cancelarPedido(idPedido: Int): Response<Unit> {
        return pedidoService.cancelarPedido(idPedido)
    }

    suspend fun obtenerProductosMayorDemanda():
            List<ProductoMayorDemandaResponse> {
        val response = ApiClient.pedidoService.obtenerProductosMayorDemanda()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }
}