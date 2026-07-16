package com.servicetech.app.ui.tecnico.asignados

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioResponse
import com.servicetech.app.data.remote.ApiClient
import com.servicetech.app.ui.tecnico.TecnicoMainActivity
import com.servicetech.app.ui.tecnico.actualizar.ActualizarEstadoActivity
import com.servicetech.app.ui.tecnico.reportes.MisReportesActivity
import com.servicetech.app.utils.DateUtils
import com.servicetech.app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Muestra los servicios asignados al técnico y permite
 * consultar su información antes de actualizar su estado.
 */
class AsignadosActivity : AppCompatActivity() {

    // ==========================
    // Componentes de la interfaz
    // ==========================

    private lateinit var recycler: RecyclerView
    private lateinit var btnInicio: LinearLayout
    private lateinit var btnReportes: LinearLayout
    private lateinit var btnLogout: LinearLayout

    // ==========================
    // Variables de sesión
    // ==========================

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tecnico_asignados_activity)

        // ==========================
        // Configuración de la interfaz
        // ==========================

        aplicarColorBarraEstado()

        sessionManager = SessionManager(this)

        recycler = findViewById(R.id.recyclerAsignados)
        recycler.layoutManager = LinearLayoutManager(this)

        configurarMenuInferior()
        cargarServicios()
    }

    // ==========================
    // Submenú inferior
    // ==========================

    /**
     * Configura las opciones de navegación
     * disponibles en la barra inferior.
     */
    private fun configurarMenuInferior() {

        btnInicio = findViewById(R.id.btnInicio)
        btnReportes = findViewById(R.id.btnReportes)
        btnLogout = findViewById(R.id.btnLogout)

        btnInicio.setOnClickListener {
            startActivity(Intent(this, TecnicoMainActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.btnAsignados).setOnClickListener {
            /* no-op */
        }

        btnReportes.setOnClickListener {
            startActivity(Intent(this, MisReportesActivity::class.java))
            finish()
        }

        btnLogout.setOnClickListener {
            sessionManager.logout()
            finishAffinity()
        }
    }

    // ==========================
    // Carga de información
    // ==========================

    /**
     * Obtiene los servicios asignados al técnico
     * y los muestra en el RecyclerView.
     */
    private fun cargarServicios() {

        val idTecnico = SessionManager(this).getUserId()

        if (idTecnico <= 0) {
            Toast.makeText(this, "Sesión inválida (sin id de usuario)", Toast.LENGTH_LONG).show()
            return
        }

        val call = ApiClient.apiService.getServiciosAsignados(idTecnico)

        call.enqueue(object : Callback<List<ServicioResponse>> {

            override fun onResponse(
                call: Call<List<ServicioResponse>>,
                response: Response<List<ServicioResponse>>
            ) {

                if (response.isSuccessful) {

                    val items = response.body().orEmpty()

                    recycler.adapter = AsignadosAdapter(items) { servicio ->
                        mostrarDetallesServicio(servicio)
                    }

                    if (items.isEmpty()) {
                        Toast.makeText(
                            this@AsignadosActivity,
                            "No hay servicios asignados",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    return
                }

                Toast.makeText(
                    this@AsignadosActivity,
                    "Servidor ${response.code()}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onFailure(
                call: Call<List<ServicioResponse>>,
                t: Throwable
            ) {

                Toast.makeText(
                    this@AsignadosActivity,
                    "Error de conexión",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    // ==========================
    // Gestión de servicios
    // ==========================

    /**
     * Muestra la información del servicio seleccionado
     * y permite acceder a la pantalla de actualización.
     */
    private fun mostrarDetallesServicio(servicio: ServicioResponse) {

        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(
            R.layout.tecnico_detalle_servicio_dialog,
            null
        )

        view.findViewById<TextView>(R.id.txtDetalleTipo).text =
            "Servicio: ${servicio.tipoServicio}"

        view.findViewById<TextView>(R.id.txtDetalleEstado).text =
            "Estado: ${servicio.estado}"

        view.findViewById<TextView>(R.id.txtDetalleDescripcion).text =
            "Descripción: ${servicio.descripcion}"

        view.findViewById<TextView>(R.id.txtDetalleFecha).text =
            "Fecha: ${DateUtils.formatearFecha(servicio.fechaCreacion)}"

        val dialog = builder.setView(view).create()

        view.findViewById<Button>(R.id.btnIrAActualizar).setOnClickListener {
            dialog.dismiss()

            val intent = Intent(this, ActualizarEstadoActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.btnIrAActualizar)
            .setBackgroundColor(
                ContextCompat.getColor(this, R.color.color_finalizado)
            )

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
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
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_500)
    }

    companion object {
        private const val TAG = "AsignadosActivity"
    }
}