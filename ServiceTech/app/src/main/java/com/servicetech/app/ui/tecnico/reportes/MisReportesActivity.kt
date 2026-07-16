package com.servicetech.app.ui.tecnico.reportes

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ReporteTecnicoResponse
import com.servicetech.app.data.remote.ApiClient
import com.servicetech.app.ui.tecnico.TecnicoMainActivity
import com.servicetech.app.ui.tecnico.asignados.AsignadosActivity
import com.servicetech.app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Permite al técnico generar y consultar reportes
 * de los servicios atendidos en un rango de fechas.
 */
class MisReportesActivity : AppCompatActivity() {

    // ==========================
    // Componentes de la interfaz
    // ==========================

    private lateinit var btnFechaInicio: Button
    private lateinit var btnFechaFin: Button
    private lateinit var btnGenerarReporte: Button
    private lateinit var cardResumen: CardView
    private lateinit var txtResumenTitulo: TextView
    private lateinit var txtTotalServicios: TextView
    private lateinit var txtResumenEstados: TextView
    private lateinit var txtSinReporte: TextView
    private lateinit var recyclerReporte: RecyclerView

    // ==========================
    // Variables de sesión
    // ==========================

    private lateinit var sessionManager: SessionManager

    // ==========================
    // Variables del reporte
    // ==========================

    private var fechaInicio: Calendar =
        Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
        }

    private var fechaFin: Calendar = Calendar.getInstance()

    private val formatoApi =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private val formatoMostrar =
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tecnico_mis_reportes_activity)

        // ==========================
        // Configuración de la interfaz
        // ==========================

        aplicarColorBarraEstado()

        sessionManager = SessionManager(this)

        btnFechaInicio = findViewById(R.id.btnFechaInicio)
        btnFechaFin = findViewById(R.id.btnFechaFin)
        btnGenerarReporte = findViewById(R.id.btnGenerarReporte)
        cardResumen = findViewById(R.id.cardResumenReporte)
        txtResumenTitulo = findViewById(R.id.txtResumenTitulo)
        txtTotalServicios = findViewById(R.id.txtTotalServicios)
        txtResumenEstados = findViewById(R.id.txtResumenEstados)
        txtSinReporte = findViewById(R.id.txtSinReporte)
        recyclerReporte = findViewById(R.id.recyclerReporte)

        recyclerReporte.layoutManager = LinearLayoutManager(this)

        actualizarEtiquetasFechas()
        configurarSelectorFechas()
        configurarMenuInferior()

        btnGenerarReporte.setOnClickListener {
            generarReporte()
        }
    }

    // ==========================
    // Selección de fechas
    // ==========================

    /**
     * Actualiza las fechas seleccionadas
     * en los botones del formulario.
     */
    private fun actualizarEtiquetasFechas() {
        btnFechaInicio.text = "Desde: ${formatoMostrar.format(fechaInicio.time)}"
        btnFechaFin.text = "Hasta: ${formatoMostrar.format(fechaFin.time)}"
    }

    /**
     * Configura los selectores de fecha
     * para definir el periodo del reporte.
     */
    private fun configurarSelectorFechas() {

        btnFechaInicio.setOnClickListener {

            mostrarDatePicker(fechaInicio) { seleccionada ->

                fechaInicio = seleccionada

                if (fechaInicio.after(fechaFin)) {
                    fechaFin = fechaInicio.clone() as Calendar
                }

                actualizarEtiquetasFechas()
            }
        }

        btnFechaFin.setOnClickListener {

            mostrarDatePicker(fechaFin) { seleccionada ->

                fechaFin = seleccionada

                if (fechaFin.before(fechaInicio)) {
                    fechaInicio = fechaFin.clone() as Calendar
                }

                actualizarEtiquetasFechas()
            }
        }
    }

    /**
     * Muestra un calendario para seleccionar
     * una fecha del periodo.
     */
    private fun mostrarDatePicker(
        fechaActual: Calendar,
        onSeleccion: (Calendar) -> Unit
    ) {

        DatePickerDialog(
            this,
            { _, year, month, day ->

                val cal = Calendar.getInstance()

                cal.set(year, month, day, 0, 0, 0)
                cal.set(Calendar.MILLISECOND, 0)

                onSeleccion(cal)
            },
            fechaActual.get(Calendar.YEAR),
            fechaActual.get(Calendar.MONTH),
            fechaActual.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // ==========================
    // Generación del reporte
    // ==========================

    /**
     * Solicita al servidor el reporte
     * correspondiente al periodo seleccionado.
     */
    private fun generarReporte() {

        val idTecnico = sessionManager.getUserId()

        if (idTecnico <= 0) {
            Toast.makeText(this, "Sesión inválida", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        if (fechaInicio.after(fechaFin)) {
            Toast.makeText(
                this,
                "La fecha de inicio no puede ser posterior a la fin",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        btnGenerarReporte.isEnabled = false

        ApiClient.apiService.obtenerReporteTecnico(
            idTecnico,
            formatoApi.format(fechaInicio.time),
            formatoApi.format(fechaFin.time)
        ).enqueue(object : Callback<ReporteTecnicoResponse> {

            override fun onResponse(
                call: Call<ReporteTecnicoResponse>,
                response: Response<ReporteTecnicoResponse>
            ) {

                btnGenerarReporte.isEnabled = true

                if (response.isSuccessful && response.body() != null) {
                    mostrarReporte(response.body()!!)
                    return
                }

                Toast.makeText(
                    this@MisReportesActivity,
                    "No se pudo generar el reporte",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(
                call: Call<ReporteTecnicoResponse>,
                t: Throwable
            ) {

                btnGenerarReporte.isEnabled = true

                Toast.makeText(
                    this@MisReportesActivity,
                    "Error de conexión",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    /**
     * Muestra el resumen y el detalle
     * de los servicios incluidos en el reporte.
     */
    private fun mostrarReporte(reporte: ReporteTecnicoResponse) {

        cardResumen.visibility = View.VISIBLE
        txtSinReporte.visibility = View.GONE

        txtResumenTitulo.text =
            "Reporte de ${reporte.nombreTecnico}\n" +
                    "${reporte.fechaInicio}  →  ${reporte.fechaFin}"

        txtTotalServicios.text =
            "Total de servicios atendidos: ${reporte.totalServicios}"

        txtResumenEstados.text = buildString {
            append("Pendientes: ${reporte.pendientes}\n")
            append("Asignados: ${reporte.asignados}\n")
            append("En proceso: ${reporte.enProceso}\n")
            append("Finalizados: ${reporte.finalizados}\n")
            append("Cancelados: ${reporte.cancelados}")
        }

        if (reporte.servicios.isEmpty()) {

            txtSinReporte.visibility = View.VISIBLE
            txtSinReporte.text =
                "No hay servicios en el periodo seleccionado"

            recyclerReporte.adapter = null

        } else {

            recyclerReporte.adapter =
                ReporteAdapter(reporte.servicios)
        }
    }

    // ==========================
    // Submenú inferior
    // ==========================

    /**
     * Configura las opciones de navegación
     * disponibles en la barra inferior.
     */
    private fun configurarMenuInferior() {

        findViewById<LinearLayout>(R.id.btnInicio).setOnClickListener {
            startActivity(Intent(this, TecnicoMainActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.btnAsignados).setOnClickListener {
            startActivity(Intent(this, AsignadosActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.btnReportes).setOnClickListener {
            /* Pantalla actual */
        }

        findViewById<LinearLayout>(R.id.btnLogout).setOnClickListener {
            sessionManager.logout()
            finishAffinity()
        }
    }

    // ==========================
    // Configuración visual
    // ==========================

    /**
     * Personaliza el color de la barra de estado
     * para mantener la identidad visual del módulo.
     */
    @Suppress("DEPRECATION")
    private fun aplicarColorBarraEstado() {
        window.statusBarColor =
            ContextCompat.getColor(this, R.color.blue_500)
    }
}