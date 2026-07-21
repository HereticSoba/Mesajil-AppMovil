package com.mesajil.app.preferences

import android.content.Context

class SessionManager(context: Context) {
    companion object {
        private const val PREF_NAME = "mesajil_session"
        private const val KEY_TOKEN = "token"
        private const val KEY_ID_USUARIO = "idUsuario"
        private const val KEY_NOMBRES = "nombres"
        private const val KEY_CORREO = "correo"
        private const val KEY_ID_ROL = "idRol"
        private const val KEY_ULTIMO_CORREO = "ultimoCorreo"
    }

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun guardarSesion(token: String, idUsuario: Int, nombres: String, correo: String, idRol: Int) {
        preferences.edit().apply {
            putString(KEY_TOKEN, token)
            putInt(KEY_ID_USUARIO, idUsuario)
            putString(KEY_NOMBRES, nombres)
            putString(KEY_CORREO, correo)
            putInt(KEY_ID_ROL, idRol)

            apply()
        }
    }

    fun obtenerToken(): String? =
        preferences.getString(KEY_TOKEN, null)

    fun obtenerIdUsuario(): Int =
        preferences.getInt(KEY_ID_USUARIO, 0)

    fun obtenerNombre(): String? =
        preferences.getString(KEY_NOMBRES, "")

    fun obtenerCorreo(): String? =
        preferences.getString(KEY_CORREO, "")

    fun obtenerIdRol(): Int =
        preferences.getInt(KEY_ID_ROL, 0)

    fun cerrarSesion() {
        preferences.edit().apply{
            remove(KEY_TOKEN)
            remove(KEY_ID_USUARIO)
            remove(KEY_NOMBRES)
            remove(KEY_CORREO)
            remove(KEY_ID_ROL)
            apply()
        }
    }

    fun haySesion(): Boolean {
        return !obtenerToken().isNullOrEmpty()
    }

    fun guardarUltimoCorreo(correo: String) {
        preferences.edit().putString(KEY_ULTIMO_CORREO, correo).apply()
    }

    fun obtenerUltimoCorreo(): String {
        return preferences.getString(KEY_ULTIMO_CORREO, "") ?: ""
    }
}