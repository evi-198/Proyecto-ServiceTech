package com.servicetech.app.data.model

/**
 * Representa la información de un usuario
 * registrado en el sistema.
 */

data class Usuario(
    val idUsuario: Int,
    val nombre: String,
    val correo: String,
    val telefono: String?,
    val rol: String
)