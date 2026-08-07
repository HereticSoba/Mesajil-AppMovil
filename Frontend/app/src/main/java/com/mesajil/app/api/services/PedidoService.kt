package com.mesajil.app.api.services

import com.mesajil.app.models.request.FinalizarCompraRequest
import com.mesajil.app.models.response.PedidoDetalleResponse
import com.mesajil.app.models.response.PedidoFinalizadoResponse
import com.mesajil.app.models.response.PedidoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PedidoService {
    @POST("api/Pedido/finalizar")
    suspend fun finalizarCompra(@Body request: FinalizarCompraRequest):
            Response<PedidoFinalizadoResponse>

    @GET("api/Pedido/mis-pedidos")
    suspend fun obtenerMisPedidos():
            Response<List<PedidoResponse>>

    @GET("api/Pedido/{id}/detalle")
    suspend fun obtenerDetallePedido(
        @Path("id") idPedido: Int
    ): Response<PedidoDetalleResponse>
}