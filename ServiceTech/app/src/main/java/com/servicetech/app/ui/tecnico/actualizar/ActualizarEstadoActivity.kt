package com.servicetech.app.ui.tecnico.actualizar

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ActualizarEstadoRequest
import com.servicetech.app.data.model.ServicioResponse
import com.servicetech.app.data.remote.ApiClient
import com.servicetech.app.ui.tecnico.TecnicoMainActivity
import com.servicetech.app.ui.tecnico.asignados.AsignadosActivity
import com.servicetech.app.ui.tecnico.reportes.MisReportesActivity
import com.servicetech.app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Permite al técnico visualizar sus servicios asignados y actualizar
 * el estado de cada uno desde una lista disponible.
 */
class ActualizarEstadoActivity : AppCompatActivity() {

    // ==========================
    // Componentes de la interfaz
    // ==========================

    private lateinit var recyclerView: RecyclerView

    // ==========================
    // Variables de sesión
    // ==========================

    private lateinit var sessionManager: SessionManager

    // ==========================
    // Adaptador
    // ==========================

    private lateinit var adapter: ActualizarEstadoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tecnico_actualizar_estado_activity)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ==========================
        // Configuración de la interfaz
        // ==========================

        recyclerView = findViewById(R.id.recyclerActualizarEstado)
        recyclerView.layoutManager = LinearLayoutManager(this)

        sessionManager = SessionManager(this)

        cargarServiciosAsignados()
        configurarMenuInferior()
    }

    // ==========================
    // Submenú inferior
    // ==========================

    /**
     * Configura la navegación entre las principales
     * opciones del panel técnico.
     */
    private fun configurarMenuInferior() {

        findViewById<LinearLayout>(R.id.btnInicio).setOnClickListener {
            val intent = Intent(this, TecnicoMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
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
            Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        ApiClient.apiService.getServiciosAsignados(idTecnico)
            .enqueue(object : Callback<List<ServicioResponse>> {

                override fun onResponse(
                    call: Call<List<ServicioResponse>>,
                    response: Response<List<ServicioResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {

                        val lista = response.body()!!.toMutableList()

                        adapter = ActualizarEstadoAdapter(lista) { servicio ->
                            mostrarDialogoCambioEstado(servicio)
                        }

                        recyclerView.adapter = adapter

                    } else {

                        Toast.makeText(
                            this@ActualizarEstadoActivity,
                            "No se pudieron cargar los servicios",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<ServicioResponse>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@ActualizarEstadoActivity,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // ==========================
    // Actualización de estado
    // ==========================

    /**
     * Muestra un diálogo con los estados disponibles
     * para actualizar el servicio seleccionado.
     */
    private fun mostrarDialogoCambioEstado(servicio: ServicioResponse) {

        val todosLosEstados = arrayOf(
            "PENDIENTE",
            "ASIGNADO",
            "EN PROCESO",
            "FINALIZADO",
            "CANCELADO"
        )

        val opciones = todosLosEstados.filter {
            !it.equals(servicio.estado.trim(), ignoreCase = true)
        }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Cambiar estado")
            .setItems(opciones) { _, index ->

                val nuevoEstado = opciones[index]
                val idEstado = estadoNombreAId(nuevoEstado)

                if (idEstado == null) {
                    Toast.makeText(this, "Estado no reconocido: $nuevoEstado", Toast.LENGTH_SHORT).show()
                    return@setItems
                }

                actualizarEstadoServicio(servicio, idEstado)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Convierte el nombre del estado en su identificador
     * correspondiente para enviarlo al servidor.
     */
    private fun estadoNombreAId(nombre: String): Int? {
        return when (nombre.trim().uppercase()) {
            "PENDIENTE" -> 1
            "ASIGNADO" -> 2
            "EN PROCESO" -> 3
            "FINALIZADO" -> 4
            "CANCELADO" -> 5
            else -> null
        }
    }

    /**
     * Envía la actualización del estado al servidor
     * y refresca la información mostrada en la lista.
     */
    private fun actualizarEstadoServicio(servicio: ServicioResponse, idEstado: Int) {

        ApiClient.apiService
            .actualizarEstadoServicio(
                servicio.idServicio,
                ActualizarEstadoRequest(idEstado)
            )
            .enqueue(object : Callback<ServicioResponse> {

                override fun onResponse(
                    call: Call<ServicioResponse>,
                    response: Response<ServicioResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {

                        val servicioActualizado = response.body()!!

                        adapter.actualizarServicio(servicioActualizado)

                        Toast.makeText(
                            this@ActualizarEstadoActivity,
                            "Estado actualizado a ${servicioActualizado.estado}",
                            Toast.LENGTH_SHORT
                        ).show()

                        return
                    }

                    Toast.makeText(
                        this@ActualizarEstadoActivity,
                        "No se pudo actualizar el estado. Intenta de nuevo.",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onFailure(
                    call: Call<ServicioResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@ActualizarEstadoActivity,
                        "Error de conexión al actualizar estado",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}