package com.servicetech.app.utils

object Constants {

    /**
     * Base del API (retrofit exige la barra final).
     * Emulador + backend en el mismo PC: [10.0.2.2] apunta al host.
     * Dispositivo físico: pon aquí la IP LAN de la máquina donde corre el backend.
     */
    const val BASE_URL = "http://10.0.2.2:8080/"

    const val PREF_NAME = "servicetech_session"

    const val KEY_ID_USUARIO = "idUsuario"
    const val KEY_NOMBRE = "nombre"
    const val KEY_CORREO = "correo"
    const val KEY_ROL = "rol"

    const val ROL_CLIENTE = "CLIENTE"
    const val ROL_TECNICO = "TECNICO"
    const val ROL_ADMIN = "ADMIN"
}
