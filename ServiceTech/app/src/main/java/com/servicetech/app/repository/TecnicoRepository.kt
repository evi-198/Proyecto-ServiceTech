package com.servicetech.app.repository

import com.servicetech.app.data.model.ServicioResponse
import com.servicetech.app.data.model.TecnicoResponse
import com.servicetech.app.data.remote.ApiClient
import retrofit2.Call

/**
 * Repositorio para consultar información relacionada con técnicos.
 *
 * Se usa principalmente desde los módulos administrativos y de seguimiento técnico.
 */
class TecnicoRepository {

    /**
     * Obtiene la lista de técnicos registrados en el sistema.
     */
    fun listarTecnicos(): Call<List<TecnicoResponse>> {
        return ApiClient.apiService.listarTecnicos()
    }

    /**
     * Obtiene las órdenes asociadas a un técnico específico.
     */
    fun listarOrdenesDelTecnico(idTecnico: Int): Call<List<ServicioResponse>> {
        return ApiClient.apiService.listarOrdenesDelTecnico(idTecnico)
    }
}