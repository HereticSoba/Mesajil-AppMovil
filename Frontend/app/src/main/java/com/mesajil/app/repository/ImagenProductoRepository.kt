package com.mesajil.app.repository

import com.mesajil.app.api.client.ApiClient
import com.mesajil.app.models.response.ImagenProductoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class ImagenProductoRepository {
    suspend fun subirImagen(
        idProducto: RequestBody,
        principal: RequestBody,
        imagen: MultipartBody.Part
    ): Response<ImagenProductoResponse> {
        return ApiClient.imagenProductoService.subirImagen(idProducto, principal, imagen)
    }

    suspend fun obtenerImagenes():
            Response<List<ImagenProductoResponse>> {
        return ApiClient.imagenProductoService.obtenerImagenes()
    }

    suspend fun actualizarImagen(
        idImagen: Int,
        idImagenBody: RequestBody,
        idProducto: RequestBody,
        principal: RequestBody,
        imagen: MultipartBody.Part?
    ): Response<Unit> {
        return ApiClient.imagenProductoService.actualizarImagen(
            idImagen, idImagenBody, idProducto, principal, imagen
        )
    }
    suspend fun eliminarImagen(
        idImagen: Int
    ): Response<Unit>{
        return ApiClient.imagenProductoService.eliminarImagen(
            idImagen
        )
    }
}