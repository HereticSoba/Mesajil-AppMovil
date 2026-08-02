package com.mesajil.app.api.client

import com.mesajil.app.api.services.AuthService
import com.mesajil.app.api.services.ProductoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.mesajil.app.api.services.CarritoService
import com.mesajil.app.api.services.DetalleCarritoService
import com.mesajil.app.api.interceptor.AuthInterceptor
import com.mesajil.app.api.services.CategoriaService
import com.mesajil.app.preferences.SessionProvider
import okhttp3.OkHttpClient
import com.mesajil.app.api.services.UsuarioService
import com.mesajil.app.api.services.PedidoService

object ApiClient {
    private const val BASE_URL = "http://192.168.100.54:5228/"
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                AuthInterceptor {
                    SessionProvider.obtenerToken()
                }
            )
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }
    val productoService: ProductoService by lazy {
        retrofit.create(ProductoService::class.java)
    }
    val carritoService: CarritoService by lazy {
        retrofit.create(CarritoService::class.java)
    }
    val detalleCarritoService: DetalleCarritoService by lazy {
        retrofit.create(DetalleCarritoService::class.java)
    }
    val usuarioService: UsuarioService by lazy {
        retrofit.create(UsuarioService::class.java)
    }
    val pedidoService: PedidoService by lazy {
        retrofit.create(PedidoService::class.java)
    }
    val categoriaService: CategoriaService by lazy {
        retrofit.create(CategoriaService::class.java)
    }
}