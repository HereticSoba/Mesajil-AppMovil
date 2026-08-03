package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import com.mesajil.app.models.request.PagoRequest

class PagoRepository {
    suspend fun procesarPago(request: PagoRequest) =
        ApiClient.pagoService.procesarPago(request)
}