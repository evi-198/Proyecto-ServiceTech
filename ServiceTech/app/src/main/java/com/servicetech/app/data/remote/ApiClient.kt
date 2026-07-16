package com.servicetech.app.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import com.servicetech.app.utils.SessionManager
import okhttp3.OkHttpClient

/**
 * Cliente principal de Retrofit para el backend de ServiceTech.
 *
 * Seguridad JWT: el backend exige el header "Authorization: Bearer <token>"
 * en cada request protegido. Este archivo lo agrega automáticamente,
 * leyendo el token que LoginActivity guardó en SessionManager tras el login.
 * Así ninguna pantalla tiene que armar ese header a mano.
 *
 * Nota: la URL actual apunta a una IP de red local, útil para pruebas desde un
 * dispositivo físico.
 */
object ApiClient {

    // Base URL del backend accesible desde la red local.
    private const val BASE_URL = "http://10.34.13.169:8080/"

    // Contexto necesario para leer el token guardado en SessionManager.
    private lateinit var appContext: Context

    /** Se llama una sola vez al iniciar la app (ver ServiceTechApp). */
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // Agrega el token (si existe) como header Authorization a cada request.
    private val authInterceptor = okhttp3.Interceptor { chain ->
        val token = SessionManager(appContext).getToken()
        val requestOriginal = chain.request()
        val requestConToken = if (token != null) {
            requestOriginal.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            requestOriginal
        }
        chain.proceed(requestConToken)
    }

    // Cliente HTTP con el interceptor de arriba ya enganchado.
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    /**
     * Instancia perezosa del servicio remoto.
     * Se crea solo cuando se necesita por primera vez.
     */
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // usa el cliente con el token, no el default
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}