package com.servicetech.app.repository

import com.servicetech.app.data.model.TipoServicio
import com.servicetech.app.data.model.TipoServicioRequest
import com.servicetech.app.data.remote.ApiClient
import retrofit2.Call

/**
 * Repositorio para administrar los tipos de servicio del sistema.
 *
 * Permite listar, crear, actualizar y eliminar categorías de servicios desde
 * el panel de administración.
 */
class TipoServicioRepository {

    /**
     * Obtiene todos los tipos de servicio disponibles.
     */
    fun listarTipos(): Call<List<TipoServicio>> {
        return ApiClient.apiService.listarTiposServicio()
    }

    /**
     * Crea un nuevo tipo de servicio.
     */
    fun crearTipo(request: TipoServicioRequest): Call<TipoServicio> {
        return ApiClient.apiService.crearTipoServicio(request)
    }

    /**
     * Actualiza un tipo de servicio existente.
     */
    fun actualizarTipo(id: Int, request: TipoServicioRequest): Call<TipoServicio> {
        return ApiClient.apiService.actualizarTipoServicio(id, request)
    }

    /**
     * Elimina un tipo de servicio por su identificador.
     */
    fun eliminarTipo(id: Int): Call<Void> {
        return ApiClient.apiService.eliminarTipoServicio(id)
    }
}