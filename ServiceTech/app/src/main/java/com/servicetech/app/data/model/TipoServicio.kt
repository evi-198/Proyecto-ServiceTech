package com.servicetech.app.data.model

/**
 * Representa la información de un tipo
 * de servicio disponible en el sistema.
 */

data class TipoServicio(
    val idTipoServicio: Int,
    val nombre: String,
    val descripcion: String?
)
