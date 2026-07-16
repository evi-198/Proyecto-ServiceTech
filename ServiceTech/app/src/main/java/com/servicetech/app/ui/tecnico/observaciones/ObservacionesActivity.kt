package com.servicetech.app.ui.tecnico.observaciones

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ObservacionRequest
import com.servicetech.app.data.model.ObservacionResponse
import com.servicetech.app.data.model.ServicioResponse
import com.servicetech.app.data.remote.ApiClient
import com.servicetech.app.ui.tecnico.TecnicoMainActivity
import com.servicetech.app.ui.tecnico.asignados.AsignadosActivity
import com.servicetech.app.ui.tecnico.reportes.MisReportesActivity
import com.servicetech.app.ui.tecnico.asignados.AsignadosAdapter
import com.servicetech.app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Permite al técnico consultar los servicios asignados,
 * registrar observaciones y visualizar su historial.
 */
class ObservacionesActivity : AppCompatActivity() {

    // ==========================
    // Componentes de la interfaz
    // ==========================

    private lateinit var recycler: RecyclerView

    // ==========================
    // Variables de sesión
    // ==========================

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tecnico_observaciones_activity)

        // ==========================
        // Configuración de la interfaz
        // ==========================

        aplicarColorBarraEstado()

        sessionManager = SessionManager(this)

        recycler = findViewById(R.id.recyclerObservaciones)
        recycler.layoutManager = LinearLayoutManager(this)

        configurarMenuInferior()
        cargarServiciosAsignados()
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
            startActivity(Intent(this, MisReportesActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.btnLogout).setOnClickListener {
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
    private fun cargarServiciosAsignados() {

        val idTecnico = sessionManager.getUserId()

        if (idTecnico <= 0) {
            Toast.makeText(this, "Sesión inválida", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        ApiClient.apiService.getServiciosAsignados(idTecnico)
            .enqueue(object : Callback<List<ServicioResponse>> {

                override fun onResponse(
                    call: Call<List<ServicioResponse>>,
                    response: Response<List<ServicioResponse>>
                ) {

                    if (response.isSuccessful) {

                        val items = response.body().orEmpty()

                        recycler.adapter = AsignadosAdapter(items) { servicio ->
                            mostrarDialogoObservaciones(servicio)
                        }

                        if (items.isEmpty()) {
                            Toast.makeText(
                                this@ObservacionesActivity,
                                "No hay servicios asignados",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        return
                    }

                    Toast.makeText(
                        this@ObservacionesActivity,
                        "Error al cargar servicios",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(
                    call: Call<List<ServicioResponse>>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@ObservacionesActivity,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // ==========================
    // Gestión de observaciones
    // ==========================

    /**
     * Muestra el historial de observaciones del servicio
     * seleccionado y permite registrar nuevas observaciones.
     */
    private fun mostrarDialogoObservaciones(servicio: ServicioResponse) {

        val view = layoutInflater.inflate(
            R.layout.tecnico_observaciones_servicio_dialog,
            null
        )

        val txtTitulo = view.findViewById<TextView>(R.id.txtTituloServicio)
        val recyclerObs = view.findViewById<RecyclerView>(R.id.recyclerObservacionesDialog)
        val txtSinObs = view.findViewById<TextView>(R.id.txtSinObservaciones)
        val edtComentario = view.findViewById<EditText>(R.id.edtNuevaObservacion)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarObservacion)

        txtTitulo.text = "${servicio.tipoServicio} — #${servicio.idServicio}"

        recyclerObs.layoutManager = LinearLayoutManager(this)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        /**
         * Carga el historial de observaciones
         * del servicio seleccionado.
         */
        fun cargarObservaciones() {

            ApiClient.apiService.listarObservaciones(servicio.idServicio)
                .enqueue(object : Callback<List<ObservacionResponse>> {

                    override fun onResponse(
                        call: Call<List<ObservacionResponse>>,
                        response: Response<List<ObservacionResponse>>
                    ) {

                        if (!response.isSuccessful) return

                        val lista = response.body().orEmpty()

                        if (lista.isEmpty()) {

                            txtSinObs.visibility = View.VISIBLE
                            recyclerObs.visibility = View.GONE

                        } else {

                            txtSinObs.visibility = View.GONE
                            recyclerObs.visibility = View.VISIBLE

                            recyclerObs.adapter = ObservacionAdapter(lista) { observacion ->
                                confirmarEliminacion(observacion) {
                                    cargarObservaciones()
                                }
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<List<ObservacionResponse>>,
                        t: Throwable
                    ) {

                        Toast.makeText(
                            this@ObservacionesActivity,
                            "Error al cargar observaciones",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

        // Registra una nueva observación para el servicio seleccionado
        btnGuardar.setOnClickListener {

            val comentario = edtComentario.text.toString().trim()

            if (comentario.isEmpty()) {
                edtComentario.error = "Escribe un comentario"
                return@setOnClickListener
            }

            val request = ObservacionRequest(
                comentario = comentario,
                idServicio = servicio.idServicio,
                idTecnico = sessionManager.getUserId()
            )

            ApiClient.apiService.registrarObservacion(request)
                .enqueue(object : Callback<ObservacionResponse> {

                    override fun onResponse(
                        call: Call<ObservacionResponse>,
                        response: Response<ObservacionResponse>
                    ) {

                        if (response.isSuccessful) {

                            edtComentario.text.clear()

                            Toast.makeText(
                                this@ObservacionesActivity,
                                "Observación registrada",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Actualiza el historial con la nueva observación
                            cargarObservaciones()

                            return
                        }

                        Toast.makeText(
                            this@ObservacionesActivity,
                            "No se pudo guardar la observación",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(
                        call: Call<ObservacionResponse>,
                        t: Throwable
                    ) {

                        Toast.makeText(
                            this@ObservacionesActivity,
                            "Error de conexión",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

        // Carga el historial antes de mostrar el diálogo
        cargarObservaciones()

        dialog.show()
    }

    /**
     * Solicita confirmación antes de eliminar
     * una observación registrada.
     */
    private fun confirmarEliminacion(
        observacion: ObservacionResponse,
        onEliminado: () -> Unit
    ) {

        AlertDialog.Builder(this)
            .setTitle("Eliminar observación")
            .setMessage("¿Deseas eliminar esta observación?")
            .setPositiveButton("Sí") { _, _ ->
                eliminarObservacion(observacion.idObservacion, onEliminado)
            }
            .setNegativeButton("No", null)
            .show()
    }

    /**
     * Elimina la observación seleccionada
     * y actualiza el listado mostrado.
     */
    private fun eliminarObservacion(
        idObservacion: Int,
        onEliminado: () -> Unit
    ) {

        ApiClient.apiService.eliminarObservacion(idObservacion)
            .enqueue(object : Callback<Void> {

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {

                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@ObservacionesActivity,
                            "Observación eliminada",
                            Toast.LENGTH_SHORT
                        ).show()

                        onEliminado()

                    } else {

                        Toast.makeText(
                            this@ObservacionesActivity,
                            "No se pudo eliminar la observación",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<Void>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@ObservacionesActivity,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
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
        private const val TAG = "ObservacionesActivity"
    }
}