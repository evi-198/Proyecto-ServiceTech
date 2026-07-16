package com.servicetech.app.data.model

/**
 * Representa la respuesta recibida del servidor
 * con la información de un servicio.
 */

data class ServicioResponse(
    val idServicio: Int,
    val descripcion: String,
    val fechaCreacion: String,
    val estado: String,
    val tipoServicio: String,
    val fechaFinalizacion: String?
)
