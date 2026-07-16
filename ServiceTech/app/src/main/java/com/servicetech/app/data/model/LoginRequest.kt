package com.servicetech.app.data.model

/**
 * Representa la solicitud enviada al servidor
 * para autenticar a un usuario en el sistema.
 */
data class LoginRequest(
    val correo: String,
    val password: String
)