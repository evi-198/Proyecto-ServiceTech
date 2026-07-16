package com.servicetech.app.ui.cliente.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioResponse
import com.servicetech.app.utils.DateUtils

/**
 * Adapter para mostrar los servicios activos del cliente con su estado actual.
 *
 * Permite filtrar la lista y resaltar visualmente cada estado para facilitar el seguimiento.
 */
class VerEstadoAdapter(
    private var lista: List<ServicioResponse>

) : RecyclerView.Adapter<VerEstadoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descripcion: TextView = view.findViewById(R.id.txtDescripcion)
        val estado: TextView = view.findViewById(R.id.txtEstado)
        val fecha: TextView = view.findViewById(R.id.txtFecha)
        val tipo: TextView = view.findViewById(R.id.txtTipo)
        val viewLinea: View = view.findViewById(R.id.viewLinea)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cliente_servicio_ver_estado_item, parent, false)
        return ViewHolder(view)
    }

    fun updateList(nuevaLista: List<ServicioResponse>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }


    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val servicio = lista[position]

        holder.descripcion.text = servicio.descripcion
        
        // Aplicamos el formato amigable de fecha
        val fechaMostrar = servicio.fechaFinalizacion ?: servicio.fechaCreacion
        holder.fecha.text = "Fecha: ${DateUtils.formatearFecha(fechaMostrar)}"
        
        holder.tipo.text = "Tipo: ${servicio.tipoServicio}"
        holder.estado.text = servicio.estado

        when (servicio.estado.trim().uppercase()) {

            "PENDIENTE" -> {
                holder.estado.setBackgroundResource(R.drawable.bg_estado_pendiente)
                holder.viewLinea.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.yellow)
                )
            }

            "EN PROCESO" -> {
                holder.estado.setBackgroundResource(R.drawable.bg_estado_proceso)
                holder.viewLinea.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.blue)
                )
            }

            "FINALIZADO", "COMPLETADO" -> {
                holder.estado.setBackgroundResource(R.drawable.bg_estado_finalizado)
                holder.viewLinea.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.green)
                )
            }

            "CANCELADO" -> {
                holder.estado.setBackgroundResource(R.drawable.bg_estado_cancelado)
                holder.viewLinea.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.red)
                )
            }

            else -> {
                holder.estado.setBackgroundResource(R.drawable.bg_estado_proceso)
                holder.viewLinea.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.gray)
                )
            }
        }

    }
}

