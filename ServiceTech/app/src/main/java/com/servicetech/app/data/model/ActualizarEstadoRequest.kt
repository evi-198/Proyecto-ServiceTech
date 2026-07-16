package com.servicetech.app.data.model

/**
 * Representa la solicitud enviada al servidor
 * para actualizar el estado de un servicio.
 */
data class ActualizarEstadoRequest(
    val idEstadoServicio: Int
)