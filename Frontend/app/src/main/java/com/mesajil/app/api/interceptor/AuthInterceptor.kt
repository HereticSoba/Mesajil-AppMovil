package com.mesajil.app.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: () -> String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.encodedPath.contains("/api/auth/login")) {
            return chain.proceed(request)
        }
        val token = tokenProvider()
        if (token.isNullOrBlank()) {
            return chain.proceed(request)
        }
        val nuevaRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(nuevaRequest)
    }
}