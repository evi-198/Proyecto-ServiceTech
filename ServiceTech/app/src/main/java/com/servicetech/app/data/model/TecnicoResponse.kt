package com.servicetech.app.data.model

/**
 * Representa la respuesta recibida del servidor
 * con la información de un técnico.
 */

data class TecnicoResponse(
    val idUsuario: Int,
    val nombre: String,
    val correo: String,
    val telefono: String?,
    val ordenesActivas: Int,
    val ordenesFinalizadas: Int,
    val limiteOrdenes: Int,
    val saturado: Boolean
)