package com.servicetech.app.ui.admin.gestion_servicios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.servicetech.app.R
import com.servicetech.app.data.model.TipoServicio

/**
 * Adapter para mostrar los tipos de servicio en el panel de administración.
 *
 * Permite editar o eliminar cada elemento desde la lista.
 */
class AdminServicioAdapter(
    private val tipos: MutableList<TipoServicio>,
    private val onEdit: (TipoServicio) -> Unit,
    private val onDelete: (TipoServicio) -> Unit
) : RecyclerView.Adapter<AdminServicioAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombreTipo)
        val txtDescripcion: TextView = view.findViewById(R.id.txtDescripcionTipo)
        val btnEditar: MaterialButton = view.findViewById(R.id.btnEditarTipo)
        val btnEliminar: MaterialButton = view.findViewById(R.id.btnEliminarTipo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_servicio_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tipo = tipos[position]
        holder.txtNombre.text = tipo.nombre
        holder.txtDescripcion.text = tipo.descripcion?.ifBlank { "Sin descripción" } ?: "Sin descripción"
        holder.btnEditar.setOnClickListener { onEdit(tipo) }
        holder.btnEliminar.setOnClickListener { onDelete(tipo) }
    }

    override fun getItemCount(): Int = tipos.size

    fun actualizarLista(nuevaLista: List<TipoServicio>) {
        tipos.clear()
        tipos.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    fun agregarTipo(tipo: TipoServicio) {
        tipos.add(0, tipo)
        notifyItemInserted(0)
    }

    fun actualizarTipo(tipo: TipoServicio) {
        val index = tipos.indexOfFirst { it.idTipoServicio == tipo.idTipoServicio }
        if (index >= 0) {
            tipos[index] = tipo
            notifyItemChanged(index)
        }
    }

    fun eliminarTipo(tipo: TipoServicio) {
        val index = tipos.indexOfFirst { it.idTipoServicio == tipo.idTipoServicio }
        if (index >= 0) {
            tipos.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}