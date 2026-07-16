package com.servicetech.app.ui.tecnico.actualizar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioResponse
import com.servicetech.app.utils.DateUtils

/**
 * Adaptador encargado de mostrar los servicios asignados al técnico
 * y permitir la actualización de su estado.
 */
class ActualizarEstadoAdapter(
    private val lista: MutableList<ServicioResponse>,
    private val onActualizarClick: (ServicioResponse) -> Unit
) : RecyclerView.Adapter<ActualizarEstadoAdapter.ViewHolder>() {

    // ==========================
    // ViewHolder
    // ==========================

    /**
     * Contiene las referencias a los componentes
     * de cada elemento del RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTipo: TextView = view.findViewById(R.id.txtTipo)
        val txtDescripcion: TextView = view.findViewById(R.id.txtDescripcion)
        val txtEstado: TextView = view.findViewById(R.id.txtEstado)
        val txtFecha: TextView = view.findViewById(R.id.txtFecha)
        val btnActualizar: Button = view.findViewById(R.id.btnActualizarEstado)
    }

    // ==========================
    // Ciclo de vida del Adapter
    // ==========================

    /**
     * Crea la vista de cada elemento del RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tecnico_servicio_actualizar_estado_item, parent, false)
        return ViewHolder(view)
    }

    /**
     * Devuelve la cantidad de servicios disponibles.
     */
    override fun getItemCount(): Int = lista.size

    /**
     * Asocia la información del servicio con los componentes
     * visuales y configura sus eventos.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val servicio = lista[position]

        holder.txtTipo.text = servicio.tipoServicio
        holder.txtDescripcion.text = servicio.descripcion
        holder.txtEstado.text = servicio.estado
        holder.txtFecha.text = DateUtils.formatearFecha(servicio.fechaCreacion)

        holder.btnActualizar.setOnClickListener {
            onActualizarClick(servicio)
        }

        // Aplica un estilo visual según el estado del servicio
        when (servicio.estado.trim().uppercase()) {

            "PENDIENTE" -> {
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_pendiente)
                holder.txtEstado.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, android.R.color.white)
                )
            }

            "ASIGNADO" -> {
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_asignado)
                holder.txtEstado.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, android.R.color.white)
                )
            }

            "EN PROCESO" -> {
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_proceso)
                holder.txtEstado.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, android.R.color.white)
                )
            }

            "FINALIZADO", "COMPLETADO" -> {
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_finalizado)
                holder.txtEstado.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, android.R.color.white)
                )
            }

            "CANCELADO" -> {
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_cancelado)
                holder.txtEstado.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, android.R.color.white)
                )
            }

            else -> {
                holder.txtEstado.background = null
                holder.txtEstado.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.gray)
                )
            }
        }
    }

    // ==========================
    // Actualización de datos
    // ==========================

    /**
     * Actualiza la información de un servicio y refresca
     * únicamente el elemento modificado.
     */
    fun actualizarServicio(servicioActualizado: ServicioResponse) {

        val posicion = lista.indexOfFirst {
            it.idServicio == servicioActualizado.idServicio
        }

        if (posicion >= 0) {
            lista[posicion] = servicioActualizado
            notifyItemChanged(posicion)
        }
    }
}