package com.mesajil.app.api.services

import com.mesajil.app.models.response.PedidoFinalizadoResponse
import com.mesajil.app.models.response.PedidoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface PedidoService {
    @POST("api/Pedido/finalizar")
    suspend fun finalizarCompra():
            Response<PedidoFinalizadoResponse>

    @GET("api/Pedido/mis-pedidos")
    suspend fun obtenerMisPedidos():
            Response<List<PedidoResponse>>
}