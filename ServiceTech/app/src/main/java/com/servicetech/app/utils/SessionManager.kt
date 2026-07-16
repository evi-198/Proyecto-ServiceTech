package com.servicetech.app.utils

import android.content.Context
import android.content.Intent
import com.servicetech.app.ui.auth.LoginActivity

/**
 * Guarda la sesión del usuario en SharedPreferences tras el login,
 * incluyendo el token JWT que el backend devuelve. ApiClient lee ese
 * token desde aquí para agregarlo a cada request protegido.
 */
class SessionManager(private val context: Context) {

    private val prefs =
        context.getSharedPreferences("session", Context.MODE_PRIVATE)

    fun saveUser(id: Int, nombre: String, correo: String, rol: String, token: String) {
        prefs.edit().apply {
            putInt("idUsuario", id)
            putString("nombre", nombre)
            putString("correo", correo)
            putString("rol", rol)
            putString("token", token) // JWT devuelto por el login
            apply()
        }
    }

    fun getRol(): String? = prefs.getString("rol", null)

    fun isLoggedIn(): Boolean = prefs.contains("idUsuario")

    fun getUserId(): Int {
        return prefs.getInt("idUsuario", 0)
    }

    fun getNombre(): String? {
        return prefs.getString("nombre", null)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun logout() {
        clearSession()

        val intent = Intent(context, LoginActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        context.startActivity(intent)
    }

    // Token usado por ApiClient para autenticar cada request al backend.
    fun getToken(): String? {
        return prefs.getString("token", null)
    }
}