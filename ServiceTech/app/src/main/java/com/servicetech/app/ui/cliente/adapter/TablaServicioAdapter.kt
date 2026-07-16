package com.servicetech.app.ui.cliente.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioResponse

/**
 * Adapter para mostrar la lista de servicios del cliente en formato de tabla.
 *
 * Cada elemento muestra el tipo, descripción y estado con un estilo visual según el estado.
 */
class TablaServicioAdapter(
    private val listaServicios: List<ServicioResponse>
) : RecyclerView.Adapter<TablaServicioAdapter.ServicioViewHolder>() {

    class ServicioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTipo: TextView = itemView.findViewById(R.id.txtTipo)
        val txtDescripcion: TextView = itemView.findViewById(R.id.txtDescripcion)
        val txtEstado: TextView = itemView.findViewById(R.id.txtEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cliente_servicio_tabla_item, parent, false)
        return ServicioViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServicioViewHolder, position: Int) {
        val servicio = listaServicios[position]

        holder.txtTipo.text = servicio.tipoServicio
        holder.txtDescripcion.text = servicio.descripcion
        holder.txtEstado.text = servicio.estado.uppercase()

        // Aplicamos el diseño de "Badge" (Etiqueta con fondo de color)
        when (servicio.estado.trim().uppercase()) {
            "PENDIENTE" -> {
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_pendiente)
                holder.txtEstado.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
            }
            "ASIGNADO" -> {
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_asignado)
                holder.txtEstado.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
            }
            "EN PROCESO" -> {
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_proceso)
                holder.txtEstado.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
            }
            "FINALIZADO", "COMPLETADO" -> {
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_finalizado)
                holder.txtEstado.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
            }
            "CANCELADO" -> {
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_cancelado)
                holder.txtEstado.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
            }
            else -> {
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_proceso)
                holder.txtEstado.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
            }
        }
    }

    override fun getItemCount(): Int = listaServicios.size
}
