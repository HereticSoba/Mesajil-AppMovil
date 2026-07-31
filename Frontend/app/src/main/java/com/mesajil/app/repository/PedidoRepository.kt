package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient

class PedidoRepository {
    suspend fun finalizarCompra() =
        ApiClient.pedidoService.finalizarCompra()

    suspend fun obtenerMisPedidos() =
        ApiClient.pedidoService.obtenerMisPedidos()
}