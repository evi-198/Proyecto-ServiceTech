package com.servicetech.app.repository

import com.servicetech.app.data.remote.ApiService
import com.servicetech.app.data.model.*

/**
 * Repositorio encargado de las operaciones relacionadas con servicios.
 *
 * Sirve como puente entre la interfaz de usuario y las peticiones del backend
 * para crear servicios y consultar los tipos de servicio disponibles.
 */
class ServicioRepository(private val apiService: ApiService) {

    /**
     * Envía una nueva orden de servicio al backend.
     */
    suspend fun crearServicio(request: ServicioRequest): Result<ServicioResponse> {
        return try {
            val response = apiService.crearServicio(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear servicio"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene los tipos de servicio disponibles para mostrarlos en formularios.
     */
    suspend fun obtenerTiposServicio(): Result<List<TipoServicio>> {
        return try {
            val response = apiService.obtenerTiposServicio()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener tipos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
