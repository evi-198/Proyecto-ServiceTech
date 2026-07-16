package com.servicetech.app.ui.tecnico.asignados

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioResponse
import com.servicetech.app.utils.DateUtils

/**
 * Adaptador encargado de mostrar la lista de servicios
 * asignados al técnico dentro del RecyclerView.
 */
class AsignadosAdapter(
    private val lista: List<ServicioResponse>,
    private val onCardClick: (ServicioResponse) -> Unit
) : RecyclerView.Adapter<AsignadosAdapter.ViewHolder>() {

    // ==========================
    // ViewHolder
    // ==========================

    /**
     * Contiene las referencias a los componentes
     * de cada elemento de la lista.
     */
    class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val txtTipo: TextView = view.findViewById(R.id.txtTipo)
        val txtEstado: TextView = view.findViewById(R.id.txtEstado)
        val txtDescription: TextView = view.findViewById(R.id.txtDescripcion)
        val txtFecha: TextView = view.findViewById(R.id.txtFecha)
    }

    // ==========================
    // Ciclo de vida del Adapter
    // ==========================

    /**
     * Crea la vista para cada elemento del RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tecnico_servicio_asignado_item, parent, false)

        return ViewHolder(view)
    }

    /**
     * Devuelve la cantidad de servicios disponibles.
     */
    override fun getItemCount(): Int = lista.size

    /**
     * Asocia la información del servicio con la vista
     * y configura el evento de selección.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val servicio = lista[position]

        holder.txtTipo.text = servicio.tipoServicio
        holder.txtEstado.text = servicio.estado
        holder.txtDescription.text = servicio.descripcion
        holder.txtFecha.text = DateUtils.formatearFecha(servicio.fechaCreacion)

        holder.itemView.setOnClickListener {
            onCardClick(servicio)
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
}