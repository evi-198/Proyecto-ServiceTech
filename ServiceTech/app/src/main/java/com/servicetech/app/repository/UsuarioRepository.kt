package com.servicetech.app.repository

import com.servicetech.app.data.model.LoginRequest
import com.servicetech.app.data.model.LoginResponse
import com.servicetech.app.data.model.Usuario
import com.servicetech.app.data.remote.ApiClient
import retrofit2.Call

/**
 * Repositorio encargado de la gestión de usuarios y autenticación.
 *
 * Centraliza las operaciones relacionadas con el acceso al sistema y la administración
 * de cuentas de usuario desde la capa de datos.
 */
class UsuarioRepository {

    /**
     * Envía las credenciales del usuario para iniciar sesión.
     */
    fun login(request: LoginRequest): Call<LoginResponse> {
        return ApiClient.apiService.login(request)
    }

    /**
     * Obtiene la lista de usuarios registrados.
     */
    fun listarUsuarios(): Call<List<Usuario>> {
        return ApiClient.apiService.listarUsuarios()
    }

    /**
     * Crea un nuevo usuario en el backend.
     */
    fun crearUsuario(request: com.servicetech.app.data.model.UsuarioRequest): Call<Usuario> {
        return ApiClient.apiService.crearUsuario(request)
    }

    /**
     * Actualiza los datos de un usuario existente mediante su id.
     */
    fun actualizarUsuario(id: Int, request: com.servicetech.app.data.model.UsuarioRequest): Call<Usuario> {
        return ApiClient.apiService.actualizarUsuario(id, request)
    }

    /**
     * Elimina un usuario del sistema.
     */
    fun eliminarUsuario(id: Int): Call<Void> {
        return ApiClient.apiService.eliminarUsuario(id)
    }
}
