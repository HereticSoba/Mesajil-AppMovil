package com.mesajil.app.api.services

import com.mesajil.app.models.request.PagoRequest
import com.mesajil.app.models.response.PagoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PagoService {
    @POST("api/Pago")
    suspend fun procesarPago(
        @Body request: PagoRequest
    ): Response<PagoResponse>
}