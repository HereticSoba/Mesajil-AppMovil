package com.mesajil.app.api.services

import com.mesajil.app.models.response.ImagenProductoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ImagenProductoService {
    @Multipart
    @POST("api/ImagenProducto")
    suspend fun subirImagen(
        @Part("IdProducto") idProducto: RequestBody,
        @Part("Principal") principal: RequestBody,
        @Part imagen: MultipartBody.Part
    ): Response<ImagenProductoResponse>

    @GET("api/ImagenProducto/{id}")
    suspend fun obtenerImagen(
        @Path("id") idImagen: Int
    ): Response<ImagenProductoResponse>

    @Multipart
    @PUT("api/ImagenProducto/{id}")
    suspend fun actualizarImagen(
        @Path("id") idImagen: Int,
        @Part("idImagen") idImagenBody: RequestBody,
        @Part("idProducto") idProducto: RequestBody,
        @Part("principal") principal: RequestBody,
        @Part imagen: MultipartBody.Part?
    ): Response<Unit>

    @DELETE("api/ImagenProducto/{id}")
    suspend fun eliminarImagen(
        @Path("id") idImagen: Int
    ): Response<Unit>

    @GET("api/ImagenProducto")
    suspend fun obtenerImagenes():
            Response<List<ImagenProductoResponse>>
}