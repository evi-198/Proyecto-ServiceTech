package com.servicetech.app.data.model

/**
 * Representa la solicitud enviada al servidor
 * para registrar una nueva observación de un servicio.
 */
data class ObservacionRequest(
    val comentario: String,
    val idServicio: Int,
    val idTecnico: Int
)
