package com.servicetech.app.ui.cliente.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioResponse
import com.servicetech.app.utils.DateUtils

/**
 * Adapter para mostrar las órdenes más recientes del cliente en la pantalla principal.
 *
 * Presenta el tipo de servicio, descripción, fecha y estado de cada orden.
 */
class OrdenRecienteAdapter(
    private val listaOrdenes: List<ServicioResponse>
) : RecyclerView.Adapter<OrdenRecienteAdapter.OrdenViewHolder>() {

    class OrdenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTipo: TextView = itemView.findViewById(R.id.txtTipoServicio)
        private val txtDescripcion: TextView = itemView.findViewById(R.id.txtDescripcion)
        private val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
        private val txtEstado: TextView = itemView.findViewById(R.id.txtEstado)

        fun bind(orden: ServicioResponse) {
            txtTipo.text = orden.tipoServicio
            txtDescripcion.text = orden.descripcion

            // Usamos la utilidad unificada para el formato de fecha y hora
            txtFecha.text = DateUtils.formatearFecha(orden.fechaCreacion)

            txtEstado.text = orden.estado

            // Cambiar color según estado
            when (orden.estado.lowercase()) {
                "pendiente" -> txtEstado.setTextColor(itemView.context.getColor(R.color.orange_500))
                "en progreso" -> txtEstado.setTextColor(itemView.context.getColor(R.color.blue_500))
                "completado", "finalizado" -> txtEstado.setTextColor(itemView.context.getColor(R.color.green_500))
                else -> txtEstado.setTextColor(itemView.context.getColor(R.color.gray_500))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cliente_orden_reciente_item, parent, false)
        return OrdenViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrdenViewHolder, position: Int) {
        holder.bind(listaOrdenes[position])
    }

    override fun getItemCount(): Int = listaOrdenes.size
}
