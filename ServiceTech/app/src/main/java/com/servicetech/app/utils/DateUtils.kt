package com.servicetech.app.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object DateUtils {

    /**
     * Convierte una fecha en formato ISO (ej: 2024-05-20T10:30:00) 
     * a un formato amigable (ej: 20 de Mayo, 10:30 AM)
     */
    fun formatearFecha(fechaIso: String?): String {
        if (fechaIso.isNullOrEmpty()) return "Fecha no disponible"

        return try {
            // Formato que suele venir del servidor (ISO 8601)
            val formatoEntrada = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            formatoEntrada.timeZone = TimeZone.getTimeZone("UTC")
            
            val fecha = formatoEntrada.parse(fechaIso)
            
            // Formato de salida: "20 de Mayo, 10:30 AM"
            val formatoSalida = SimpleDateFormat("dd 'de' MMMM, hh:mm a", Locale("es", "ES"))
            formatoSalida.timeZone = TimeZone.getDefault()
            
            fecha?.let { formatoSalida.format(it) } ?: fechaIso
        } catch (e: Exception) {
            // Si falla el parseo (ej: no trae la T), intentamos un formato simple
            try {
                val formatoSimple = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val fecha = formatoSimple.parse(fechaIso)
                val formatoSalida = SimpleDateFormat("dd 'de' MMMM, hh:mm a", Locale("es", "ES"))
                fecha?.let { formatoSalida.format(it) } ?: fechaIso
            } catch (e2: Exception) {
                fechaIso // Devolvemos la original si nada funciona
            }
        }
    }
}
