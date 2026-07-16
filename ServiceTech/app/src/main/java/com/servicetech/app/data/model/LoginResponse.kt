package com.servicetech.app.data.model

/**
 * Representa la respuesta recibida del servidor
 * después de autenticar correctamente a un usuario.
 */

data class LoginResponse(
    val idUsuario: Int,
    val nombre: String,
    val correo: String,
    val rol: String,
    val token: String
)