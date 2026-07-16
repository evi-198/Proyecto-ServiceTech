package com.servicetech.app.ui.tecnico.reportes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ReporteServicioDetalle
import com.servicetech.app.utils.DateUtils

/**
 * Adaptador encargado de mostrar el detalle de los servicios
 * incluidos en el reporte generado por el técnico.
 */
class ReporteAdapter(
    private val lista: List<ReporteServicioDetalle>
) : RecyclerView.Adapter<ReporteAdapter.ViewHolder>() {

    // ==========================
    // ViewHolder
    // ==========================

    /**
     * Contiene las referencias a los componentes
     * de cada elemento del RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTipo: TextView = view.findViewById(R.id.txtReporteTipo)
        val txtCliente: TextView = view.findViewById(R.id.txtReporteCliente)
        val txtEstado: TextView = view.findViewById(R.id.txtReporteEstado)
        val txtFechas: TextView = view.findViewById(R.id.txtReporteFechas)
        val txtDescripcion: TextView = view.findViewById(R.id.txtReporteDescripcion)
        val txtObsTitulo: TextView = view.findViewById(R.id.txtReporteObservacionesTitulo)
        val txtObsDetalle: TextView = view.findViewById(R.id.txtReporteObservacionesDetalle)
    }

    // ==========================
    // Ciclo de vida del Adapter
    // ==========================

    /**
     * Crea la vista para cada elemento del RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tecnico_reporte_servicio_item, parent, false)
        return ViewHolder(view)
    }

    /**
     * Devuelve la cantidad de servicios
     * incluidos en el reporte.
     */
    override fun getItemCount(): Int = lista.size

    /**
     * Asocia la información de cada servicio con la vista
     * y muestra sus observaciones registradas.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = lista[position]

        holder.txtTipo.text = "${item.tipoServicio}  #${item.idServicio}"
        holder.txtCliente.text = "Cliente: ${item.cliente}"
        holder.txtEstado.text = item.estado

        aplicarColorEstado(holder, item.estado)

        val fechaFinTexto = item.fechaFinalizacion?.let {
            DateUtils.formatearFecha(it)
        } ?: "Sin fecha de cierre"

        holder.txtFechas.text =
            "Creado: ${DateUtils.formatearFecha(item.fechaCreacion)}\n" +
                    "Finalizado: $fechaFinTexto"

        holder.txtDescripcion.text = item.descripcion

        if (item.observaciones.isEmpty()) {

            holder.txtObsTitulo.text =
                "Detalles de atención: sin observaciones"

            holder.txtObsDetalle.visibility = View.GONE

        } else {

            holder.txtObsTitulo.text =
                "Detalles de atención (${item.cantidadObservaciones} observaciones)"

            holder.txtObsDetalle.visibility = View.VISIBLE

            holder.txtObsDetalle.text =
                item.observaciones.joinToString("\n\n") { obs ->
                    "• ${DateUtils.formatearFecha(obs.fecha)}: ${obs.comentario}"
                }
        }
    }

    // ==========================
    // Configuración visual
    // ==========================

    /**
     * Aplica el color correspondiente
     * según el estado del servicio.
     */
    private fun aplicarColorEstado(holder: ViewHolder, estado: String) {

        when (estado.trim().uppercase()) {

            "PENDIENTE" ->
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_pendiente)

            "ASIGNADO" ->
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_asignado)

            "EN PROCESO" ->
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_proceso)

            "FINALIZADO", "COMPLETADO" ->
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_finalizado)

            "CANCELADO" ->
                holder.txtEstado.setBackgroundResource(R.drawable.bg_estado_cancelado)

            else -> {
                holder.txtEstado.background = null
                holder.txtEstado.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.gray)
                )
                return
            }
        }

        holder.txtEstado.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                android.R.color.white
            )
        )
    }
}