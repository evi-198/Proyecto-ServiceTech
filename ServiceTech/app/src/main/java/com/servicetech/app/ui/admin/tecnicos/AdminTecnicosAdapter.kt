package com.servicetech.app.ui.admin.tecnicos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.TecnicoResponse

/**
 * Adapter para mostrar técnicos y su carga actual en el panel administrativo.
 */
class AdminTecnicosAdapter(
    private val lista: List<TecnicoResponse>,
    private val onCardClick: (TecnicoResponse) -> Unit
) : RecyclerView.Adapter<AdminTecnicosAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombreTecnico)
        val txtCorreo: TextView = view.findViewById(R.id.txtCorreoTecnico)
        val txtTelefono: TextView = view.findViewById(R.id.txtTelefonoTecnico)
        val chipCarga: TextView = view.findViewById(R.id.chipCarga)
        val txtFinalizadas: TextView = view.findViewById(R.id.txtFinalizadasTecnico)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_tecnico_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tecnico = lista[position]
        val ctx = holder.itemView.context

        holder.txtNombre.text = tecnico.nombre
        holder.txtCorreo.text = tecnico.correo
        holder.txtTelefono.text = "Teléfono: ${tecnico.telefono ?: "-"}"
        holder.chipCarga.text = "${tecnico.ordenesActivas} / ${tecnico.limiteOrdenes} activas"
        holder.txtFinalizadas.text = "${tecnico.ordenesFinalizadas} finalizadas"

        // Aviso visual (solo referencial, no bloquea nada):
        // verde = normal, ámbar = cerca del límite, rojo = en el límite o por encima
        val colorRes = when {
            tecnico.saturado -> R.color.red
            tecnico.ordenesActivas >= (tecnico.limiteOrdenes * 0.7) -> R.color.orange_500
            else -> R.color.green
        }
        holder.chipCarga.background.setTint(ContextCompat.getColor(ctx, colorRes))

        holder.itemView.setOnClickListener {
            onCardClick(tecnico)
        }
    }
}