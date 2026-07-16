package com.servicetech.app.data.remote

import com.servicetech.app.data.model.ActualizarEstadoRequest
import com.servicetech.app.data.model.AsignarTecnicoRequest
import com.servicetech.app.data.model.LoginRequest
import com.servicetech.app.data.model.LoginResponse
import com.servicetech.app.data.model.ObservacionRequest
import com.servicetech.app.data.model.ObservacionResponse
import com.servicetech.app.data.model.ReporteTecnicoResponse
import com.servicetech.app.data.model.ServicioAdminResponse
import com.servicetech.app.data.model.ServicioRequest
import com.servicetech.app.data.model.ServicioResponse
import com.servicetech.app.data.model.TecnicoResponse
import com.servicetech.app.data.model.TipoServicio
import com.servicetech.app.data.model.TipoServicioRequest
import com.servicetech.app.data.model.Usuario
import com.servicetech.app.data.model.UsuarioRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Contrato de endpoints del backend para la aplicación ServiceTech.
 *
 * Cada metodo representa un endpoint REST y se usa desde los repositorios para
 * separar la lógica de acceso a datos de la lógica de negocio o de la interfaz.
 */
interface ApiService {

    // ── Autenticación y usuarios ───────────────────────────────────────

    /**
     * Inicia sesión del usuario con correo y contraseña.
     * Se utiliza en la pantalla de login para autenticar al usuario.
     */
    @POST("api/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    /**
     * Obtiene la lista completa de usuarios registrados.
     * Usado principalmente en el panel administrativo para gestionar cuentas.
     */
    @GET("api/usuarios")
    fun listarUsuarios(): Call<List<Usuario>>

    /**
     * Crea un nuevo usuario en el sistema.
     */
    @POST("api/usuarios")
    fun crearUsuario(@Body request: UsuarioRequest): Call<Usuario>

    /**
     * Actualiza los datos de un usuario existente identificado por su id.
     */
    @PUT("api/usuarios/{id}")
    fun actualizarUsuario(
        @Path("id") id: Int,
        @Body request: UsuarioRequest
    ): Call<Usuario>

    /**
     * Elimina un usuario del sistema mediante su identificador.
     */
    @DELETE("api/usuarios/{id}")
    fun eliminarUsuario(@Path("id") id: Int): Call<Void>

    // ── Servicios y tipos de servicio ─────────────────────────────────

    /**
     * Crea una nueva orden de servicio desde la app.
     */
    @POST("api/servicios")
    suspend fun crearServicio(@Body request: ServicioRequest): Response<ServicioResponse>

    /**
     * Obtiene los tipos de servicio disponibles para mostrar en formularios.
     */
    @GET("api/tipos-servicio")
    suspend fun obtenerTiposServicio(): Response<List<TipoServicio>>

    // ── Servicios para clientes ───────────────────────────────────────

    /**
     * Obtiene todos los servicios asociados a un cliente.
     */
    @GET("api/cliente/{id}")
    fun listarServicios(@Path("id") id: Int): Call<List<ServicioResponse>>

    /**
     * Obtiene únicamente los servicios activos de un cliente.
     */
    @GET("api/cliente/{id}/activos")
    fun listarServiciosActivos(@Path("id") id: Int): Call<List<ServicioResponse>>

    /**
     * Obtiene el historial completo de servicios de un cliente.
     */
    @GET("api/cliente/{idCliente}/historial")
    fun obtenerHistorialCliente(@Path("idCliente") idCliente: Int): Call<List<ServicioResponse>>

    // ── Servicios para técnicos ───────────────────────────────────────

    /**
     * Obtiene los servicios asignados a un técnico en particular.
     */
    @GET("api/tecnico/{id}")
    fun getServiciosAsignados(@Path("id") id: Int): Call<List<ServicioResponse>>

    /**
     * Obtiene los próximos servicios programados para un técnico.
     */
    @GET("api/tecnico/{id}/proximos")
    fun getServiciosProximos(@Path("id") id: Int): Call<List<ServicioResponse>>

    /**
     * Actualiza el estado de un servicio específico.
     * Se usa en la pantalla de actualización de estado del técnico.
     */
    @PUT("api/servicios/{idServicio}/estado")
    fun actualizarEstadoServicio(
        @Path("idServicio") idServicio: Int,
        @Body request: ActualizarEstadoRequest
    ): Call<ServicioResponse>

    // ── Observaciones del técnico ─────────────────────────────────────

    /**
     * Obtiene el historial de observaciones de un servicio.
     */
    @GET("api/servicios/{idServicio}/observaciones")
    fun listarObservaciones(@Path("idServicio") idServicio: Int): Call<List<ObservacionResponse>>

    /**
     * Registra una nueva observación asociada a un servicio y un técnico.
     */
    @POST("api/observaciones")
    fun registrarObservacion(@Body request: ObservacionRequest): Call<ObservacionResponse>

    /**
     * Elimina una observación por su identificador.
     */
    @DELETE("api/observaciones/{idObservacion}")
    fun eliminarObservacion(@Path("idObservacion") idObservacion: Int): Call<Void>

    // ── Reportes del técnico ──────────────────────────────────────────

    /**
     * Obtiene un reporte técnico filtrado por un rango de fechas.
     */
    @GET("api/tecnico/{idTecnico}/reporte")
    fun obtenerReporteTecnico(
        @Path("idTecnico") idTecnico: Int,
        @Query("fechaInicio") fechaInicio: String,
        @Query("fechaFin") fechaFin: String
    ): Call<ReporteTecnicoResponse>

    // ── Funcionalidades de administración ────────────────────────────

    /**
     * Obtiene la lista de técnicos registrados para el panel administrativo.
     */
    @GET("api/tecnicos")
    fun listarTecnicos(): Call<List<TecnicoResponse>>

    /**
     * Obtiene las órdenes asociadas a un técnico específico.
     */
    @GET("api/tecnicos/{id}/ordenes")
    fun listarOrdenesDelTecnico(@Path("id") id: Int): Call<List<ServicioResponse>>

    /**
     * Obtiene las órdenes de servicio, opcionalmente filtradas por estado.
     */
    @GET("api/servicios")
    fun listarOrdenes(@Query("estado") estado: String? = null): Call<List<ServicioAdminResponse>>

    /**
     * Asigna un técnico a una orden de servicio.
     */
    @PUT("api/servicios/{id}/tecnico")
    fun asignarTecnico(
        @Path("id") idServicio: Int,
        @Body request: AsignarTecnicoRequest
    ): Call<ServicioAdminResponse>

    /**
     * Lista los tipos de servicio disponibles para administración.
     */
    @GET("api/tipos-servicio")
    fun listarTiposServicio(): Call<List<TipoServicio>>

    /**
     * Crea un nuevo tipo de servicio desde el panel de administración.
     */
    @POST("api/tipos-servicio")
    fun crearTipoServicio(@Body request: TipoServicioRequest): Call<TipoServicio>

    /**
     * Actualiza un tipo de servicio existente.
     */
    @PUT("api/tipos-servicio/{id}")
    fun actualizarTipoServicio(
        @Path("id") id: Int,
        @Body request: TipoServicioRequest
    ): Call<TipoServicio>

    /**
     * Elimina un tipo de servicio mediante su identificador.
     */
    @DELETE("api/tipos-servicio/{id}")
    fun eliminarTipoServicio(@Path("id") id: Int): Call<Void>
}