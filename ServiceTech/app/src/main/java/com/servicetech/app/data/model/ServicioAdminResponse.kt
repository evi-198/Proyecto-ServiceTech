package com.servicetech.app.data.model

/**
 * Representa la respuesta recibida del servidor
 * con la información de un servicio para el administrador.
 */

data class ServicioAdminResponse(
    val idServicio: Int,
    val descripcion: String,
    val fechaCreacion: String,
    val estado: String,
    val tipoServicio: String,
    val fechaFinalizacion: String?,
    val idCliente: Int,
    val clienteNombre: String,
    val idTecnico: Int?,
    val tecnicoNombre: String?
)