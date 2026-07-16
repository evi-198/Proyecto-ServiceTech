package com.servicetech.app.ui.admin.ordenes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioAdminResponse
import com.google.android.material.button.MaterialButton

/**
 * Adapter para mostrar órdenes de servicio en la pantalla administrativa.
 *
 * Cada elemento presenta su estado y permite asignar o reasignar un técnico.
 */
class AdminOrdenesAdapter(
    private val lista: List<ServicioAdminResponse>,
    private val onAsignarClick: (ServicioAdminResponse) -> Unit
) : RecyclerView.Adapter<AdminOrdenesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTipo: TextView = view.findViewById(R.id.txtTipoOrden)
        val txtEstado: TextView = view.findViewById(R.id.txtEstadoOrden)
        val txtDescripcion: TextView = view.findViewById(R.id.txtDescripcionOrden)
        val txtCliente: TextView = view.findViewById(R.id.txtClienteOrden)
        val txtTecnico: TextView = view.findViewById(R.id.txtTecnicoOrden)
        val btnAsignar: MaterialButton = view.findViewById(R.id.btnAsignarTecnico)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_orden_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orden = lista[position]

        holder.txtTipo.text = orden.tipoServicio
        holder.txtDescripcion.text = orden.descripcion
        holder.txtCliente.text = "Cliente: ${orden.clienteNombre}"
        holder.txtTecnico.text = "Técnico: ${orden.tecnicoNombre ?: "Sin asignar"}"
        holder.txtEstado.text = orden.estado

        when (orden.estado.trim().uppercase()) {
            "PENDIENTE" -> holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_pendiente)
            "ASIGNADO" -> holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_asignado)
            "EN PROCESO" -> holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_proceso)
            "FINALIZADO" -> holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_finalizado)
            "CANCELADO" -> holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_cancelado)
        }

        // Si ya está finalizada o cancelada, no tiene sentido reasignar
        val editable = orden.estado.uppercase() !in listOf("FINALIZADO", "CANCELADO")
        holder.btnAsignar.visibility = if (editable) View.VISIBLE else View.GONE
        holder.btnAsignar.text = if (orden.idTecnico == null) "Asignar técnico" else "Reasignar"

        holder.btnAsignar.setOnClickListener {
            onAsignarClick(orden)
        }
    }
}