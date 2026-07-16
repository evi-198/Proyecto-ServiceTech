package com.servicetech.app.data.model

/**
 * Representa la solicitud enviada al servidor
 * para registrar una nueva solicitud de servicio.
 */

data class ServicioRequest(
    val descripcion: String,
    val idCliente: Int,
    val idTipoServicio: Int
)
