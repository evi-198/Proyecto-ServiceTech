package com.servicetech.app.data.model

/**
 * Representa la solicitud enviada al servidor
 * para asignar un técnico a un servicio.
 */
data class AsignarTecnicoRequest(
    val idTecnico: Int
)