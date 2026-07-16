package com.servicetech.app.ui.admin.ordenes

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioAdminResponse
import com.servicetech.app.data.model.TecnicoResponse
import com.servicetech.app.repository.OrdenRepository
import com.servicetech.app.repository.TecnicoRepository
import com.servicetech.app.ui.admin.gestion_servicios.AdminServiciosActivity
import com.servicetech.app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Pantalla de administración de órdenes de servicio.
 *
 * Permite visualizar órdenes, filtrar por estado y asignar técnicos desde el panel admin.
 */
class AdminOrdenesActivity : AppCompatActivity() {

    private lateinit var recyclerOrdenes: RecyclerView
    private lateinit var txtOrdenesVacio: TextView
    private lateinit var chipPendientes: TextView
    private lateinit var chipTodas: TextView

    private val ordenRepository = OrdenRepository()
    private val tecnicoRepository = TecnicoRepository()

    private lateinit var sessionManager: SessionManager
    private var filtroActual: String? = "PENDIENTE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se inicializa la pantalla y los filtros de visualización.
        setContentView(R.layout.admin_ordenes_activity)
        sessionManager = SessionManager(this)

        recyclerOrdenes = findViewById(R.id.recyclerOrdenes)
        txtOrdenesVacio = findViewById(R.id.txtOrdenesVacio)
        chipPendientes = findViewById(R.id.chipPendientes)
        chipTodas = findViewById(R.id.chipTodas)
        recyclerOrdenes.layoutManager = LinearLayoutManager(this)

        findViewById<android.view.View>(R.id.btnInicio).setOnClickListener { finish() }
        findViewById<android.view.View>(R.id.btnLogout).setOnClickListener {
            sessionManager.logout()
        }

        chipPendientes.setOnClickListener { seleccionarFiltro("PENDIENTE") }
        chipTodas.setOnClickListener { seleccionarFiltro(null) }

        cargarOrdenes()

        findViewById<android.view.View>(R.id.btnServicios).setOnClickListener {
            startActivity(Intent(this, AdminServiciosActivity::class.java))
            finish()
        }
    }

    /**
     * Cambia el filtro actual y vuelve a cargar las órdenes según el estado seleccionado.
     */
    private fun seleccionarFiltro(estado: String?) {
        filtroActual = estado

        chipPendientes.setBackgroundResource(
            if (estado == "PENDIENTE") R.drawable.bg_estado_pendiente else 0
        )
        chipPendientes.setTextColor(
            resources.getColor(if (estado == "PENDIENTE") android.R.color.white else R.color.gray, theme)
        )

        chipTodas.setBackgroundResource(
            if (estado == null) R.drawable.bg_estado_pendiente else 0
        )
        chipTodas.setTextColor(
            resources.getColor(if (estado == null) android.R.color.white else R.color.gray, theme)
        )

        cargarOrdenes()
    }

    /**
     * Solicita las órdenes al backend según el filtro actual.
     */
    private fun cargarOrdenes() {
        ordenRepository.listarOrdenes(filtroActual).enqueue(object : Callback<List<ServicioAdminResponse>> {
            override fun onResponse(
                call: Call<List<ServicioAdminResponse>>,
                response: Response<List<ServicioAdminResponse>>
            ) {
                val ordenes = response.body()
                if (response.isSuccessful && !ordenes.isNullOrEmpty()) {
                    recyclerOrdenes.visibility = android.view.View.VISIBLE
                    txtOrdenesVacio.visibility = android.view.View.GONE
                    recyclerOrdenes.adapter = AdminOrdenesAdapter(ordenes) { orden ->
                        mostrarSelectorDeTecnico(orden)
                    }
                } else {
                    recyclerOrdenes.visibility = android.view.View.GONE
                    txtOrdenesVacio.visibility = android.view.View.VISIBLE
                    txtOrdenesVacio.text = "No hay órdenes en este filtro"
                }
            }

            override fun onFailure(call: Call<List<ServicioAdminResponse>>, t: Throwable) {
                txtOrdenesVacio.visibility = android.view.View.VISIBLE
                txtOrdenesVacio.text = "Error al cargar órdenes"
                Toast.makeText(this@AdminOrdenesActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Muestra la lista de técnicos disponibles para asignar a una orden.
     */
    private fun mostrarSelectorDeTecnico(orden: ServicioAdminResponse) {
        tecnicoRepository.listarTecnicos().enqueue(object : Callback<List<TecnicoResponse>> {
            override fun onResponse(
                call: Call<List<TecnicoResponse>>,
                response: Response<List<TecnicoResponse>>
            ) {
                val tecnicos = response.body()
                if (!response.isSuccessful || tecnicos.isNullOrEmpty()) {
                    Toast.makeText(this@AdminOrdenesActivity, "No hay técnicos disponibles", Toast.LENGTH_SHORT).show()
                    return
                }

                val nombres = tecnicos.map { t ->
                    "${t.nombre}  (${t.ordenesActivas}/${t.limiteOrdenes} activas)"
                }.toTypedArray()

                AlertDialog.Builder(this@AdminOrdenesActivity)
                    .setTitle("Asignar técnico")
                    .setItems(nombres) { _, which ->
                        val tecnico = tecnicos[which]
                        if (tecnico.saturado) {
                            confirmarAsignacionSaturada(orden, tecnico)
                        } else {
                            asignarTecnico(orden.idServicio, tecnico.idUsuario)
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }

            override fun onFailure(call: Call<List<TecnicoResponse>>, t: Throwable) {
                Toast.makeText(this@AdminOrdenesActivity, "Error al cargar técnicos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Solicita confirmación cuando se intenta asignar una orden a un técnico ya saturado.
     */
    private fun confirmarAsignacionSaturada(orden: ServicioAdminResponse, tecnico: TecnicoResponse) {
        AlertDialog.Builder(this)
            .setTitle("Técnico al límite")
            .setMessage(
                "${tecnico.nombre} ya tiene ${tecnico.ordenesActivas} órdenes activas " +
                        "(su límite es ${tecnico.limiteOrdenes}). ¿Deseas asignarle esta orden de todas formas?"
            )
            .setPositiveButton("Asignar igual") { _, _ ->
                asignarTecnico(orden.idServicio, tecnico.idUsuario)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Envía la asignación de técnico a la orden seleccionada.
     */
    private fun asignarTecnico(idServicio: Int, idTecnico: Int) {
        ordenRepository.asignarTecnico(idServicio, idTecnico).enqueue(object : Callback<ServicioAdminResponse> {
            override fun onResponse(
                call: Call<ServicioAdminResponse>,
                response: Response<ServicioAdminResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AdminOrdenesActivity, "Técnico asignado", Toast.LENGTH_SHORT).show()
                    cargarOrdenes()
                } else {
                    Toast.makeText(this@AdminOrdenesActivity, "No se pudo asignar (${response.code()})", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ServicioAdminResponse>, t: Throwable) {
                Toast.makeText(this@AdminOrdenesActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}