package com.servicetech.app.data.model

/**
 * Representa la respuesta recibida del servidor
 * con la información de un reporte técnico.
 */
data class ReporteTecnicoResponse(
    val idTecnico: Int,
    val nombreTecnico: String,
    val fechaInicio: String,
    val fechaFin: String,
    val totalServicios: Int,
    val pendientes: Int,
    val asignados: Int,
    val enProceso: Int,
    val finalizados: Int,
    val cancelados: Int,
    val servicios: List<ReporteServicioDetalle>
)
