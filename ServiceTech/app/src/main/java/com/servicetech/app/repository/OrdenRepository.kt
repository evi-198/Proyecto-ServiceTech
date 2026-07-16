package com.servicetech.app.repository

import com.servicetech.app.data.model.AsignarTecnicoRequest
import com.servicetech.app.data.model.ServicioAdminResponse
import com.servicetech.app.data.remote.ApiClient
import retrofit2.Call

/**
 * Repositorio para gestionar órdenes de servicio desde el panel administrativo.
 *
 * Permite consultar órdenes y asignar técnicos a cada una de ellas.
 */
class OrdenRepository {

    /**
     * Obtiene las órdenes de servicio, opcionalmente filtradas por estado.
     */
    fun listarOrdenes(estado: String? = null): Call<List<ServicioAdminResponse>> {
        return ApiClient.apiService.listarOrdenes(estado)
    }

    /**
     * Asigna un técnico a una orden específica.
     */
    fun asignarTecnico(idServicio: Int, idTecnico: Int): Call<ServicioAdminResponse> {
        return ApiClient.apiService.asignarTecnico(idServicio, AsignarTecnicoRequest(idTecnico))
    }
}