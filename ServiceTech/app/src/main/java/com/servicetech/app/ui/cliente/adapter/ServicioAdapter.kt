package com.servicetech.app.ui.cliente.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioResponse

/**
 * Adapter simple para mostrar servicios del cliente en una lista básica.
 *
 * Se usa para representar la información clave de cada servicio con descripción y estado.
 */
class ServicioAdapter(
    private val lista: List<ServicioResponse>
) : RecyclerView.Adapter<ServicioAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descripcion: TextView = view.findViewById(R.id.txtDescripcion)
        val estado: TextView = view.findViewById(R.id.txtEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cliente_servicio_tabla_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val servicio = lista[position]
        holder.descripcion.text = servicio.descripcion
        holder.estado.text = "Estado: ${servicio.estado}"
    }
}

