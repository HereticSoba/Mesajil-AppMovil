package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import com.mesajil.app.models.request.FinalizarCompraRequest

class PedidoRepository {
    suspend fun finalizarCompra(request: FinalizarCompraRequest) =
        ApiClient.pedidoService.finalizarCompra(request)

    suspend fun obtenerMisPedidos() =
        ApiClient.pedidoService.obtenerMisPedidos()
}