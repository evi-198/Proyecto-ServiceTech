package com.servicetech.app.ui.tecnico.observaciones

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ObservacionResponse
import com.servicetech.app.utils.DateUtils

/**
 * Adaptador encargado de mostrar el historial de observaciones
 * registradas para un servicio técnico.
 */
class ObservacionAdapter(
    private val lista: List<ObservacionResponse>,
    private val onEliminarClick: (ObservacionResponse) -> Unit
) : RecyclerView.Adapter<ObservacionAdapter.ViewHolder>() {

    // ==========================
    // ViewHolder
    // ==========================

    /**
     * Contiene las referencias a los componentes
     * de cada elemento del RecyclerView.
     */
    class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val txtTecnico: TextView = view.findViewById(R.id.txtObsTecnico)
        val txtFecha: TextView = view.findViewById(R.id.txtObsFecha)
        val txtComentario: TextView = view.findViewById(R.id.txtObsComentario)
        val btnEliminar: ImageButton = view.findViewById(R.id.btnEliminarObservacion)
    }

    // ==========================
    // Ciclo de vida del Adapter
    // ==========================

    /**
     * Crea la vista para cada elemento del RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tecnico_observacion_item, parent, false)
        return ViewHolder(view)
    }

    /**
     * Devuelve la cantidad de observaciones disponibles.
     */
    override fun getItemCount(): Int = lista.size

    /**
     * Asocia la información de cada observación con la vista
     * y configura el evento para eliminar el registro.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val obs = lista[position]

        holder.txtTecnico.text = obs.tecnico
        holder.txtFecha.text = DateUtils.formatearFecha(obs.fecha)
        holder.txtComentario.text = obs.comentario

        holder.btnEliminar.setOnClickListener {
            onEliminarClick(obs)
        }
    }
}