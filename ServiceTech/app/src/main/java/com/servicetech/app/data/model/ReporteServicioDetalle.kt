package com.servicetech.app.data.model

/**
 * Representa la información detallada
 * de un servicio incluido en un reporte técnico.
 */
data class ReporteServicioDetalle(
    val idServicio: Int,
    val cliente: String,
    val tipoServicio: String,
    val estado: String,
    val descripcion: String,
    val fechaCreacion: String,
    val fechaFinalizacion: String?,
    val cantidadObservaciones: Int,
    val observaciones: List<ObservacionResumen>
)
