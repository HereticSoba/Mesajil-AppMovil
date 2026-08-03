package com.mesajil.app.preferences

import android.content.Context

object SessionProvider {
    private lateinit var sessionManager: SessionManager
    fun init(context: Context) {
        sessionManager = SessionManager(context)
    }

    fun obtenerToken(): String? {
        return sessionManager.obtenerToken()
    }

    fun obtenerCorreo(): String {
        return sessionManager.obtenerCorreo().orEmpty()
    }
}