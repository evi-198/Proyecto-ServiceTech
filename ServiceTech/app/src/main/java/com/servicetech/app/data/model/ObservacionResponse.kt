package com.servicetech.app.data.model

/**
 * Representa la respuesta recibida del servidor
 * con la información de una observación registrada.
 */
data class ObservacionResponse(
    val idObservacion: Int,
    val comentario: String,
    val fecha: String,
    val tecnico: String
)
