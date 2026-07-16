package com.servicetech.app

import android.app.Application
import com.servicetech.app.data.remote.ApiClient

/**
 * Se ejecuta una sola vez, antes que cualquier Activity, al arrancar la app.
 * Aquí se inicializa ApiClient con el contexto, para que su interceptor
 * pueda leer el token guardado en SessionManager.
 */
class ServiceTechApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ApiClient.init(applicationContext)
    }
}