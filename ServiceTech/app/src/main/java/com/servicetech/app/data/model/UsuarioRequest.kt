package com.servicetech.app.data.model

/**
 * Representa la solicitud enviada al servidor
 * para registrar o actualizar un usuario.
 */

data class UsuarioRequest(
    val nombre: String,
    val correo: String,
    val password: String?,
    val telefono: String?,
    val rol: String
)
