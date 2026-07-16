package com.servicetech.app.data.model

/**
 * Representa la solicitud enviada al servidor
 * para registrar un nuevo tipo de servicio.
 */

data class TipoServicioRequest(
    val nombre: String,
    val descripcion: String?
)