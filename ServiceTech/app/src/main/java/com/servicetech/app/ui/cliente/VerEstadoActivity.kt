package com.servicetech.app.ui.cliente

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButtonToggleGroup
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioResponse
import com.servicetech.app.data.remote.ApiClient
import com.servicetech.app.data.remote.ApiService
import com.servicetech.app.ui.cliente.adapter.VerEstadoAdapter
import com.servicetech.app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Pantalla para consultar el estado activo de los servicios del cliente.
 *
 * Permite filtrar servicios pendientes o en proceso y ver su seguimiento actual.
 */
class VerEstadoActivity : AppCompatActivity() {

    // Variables para mostrar la lista de servicios y manejar la sesión del usuario.
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VerEstadoAdapter
    private lateinit var sessionManager: SessionManager
    private var listaOriginal: List<ServicioResponse> = listOf()

    // Elementos del menú inferior para navegar entre pantallas.
    private lateinit var btnInicio: LinearLayout
    private lateinit var btnMisServicios: LinearLayout
    private lateinit var btnHistorial: LinearLayout
    private lateinit var btnLogout: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.cliente_ver_estado_activity)

        // Ajusta el color de la barra de estado para mantener la interfaz consistente.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.blue_500)
        }

        // Inicializa la sesión y vincula los elementos de la interfaz.
        sessionManager = SessionManager(this)
        recyclerView = findViewById(R.id.recyclerVerEstado)
        btnInicio = findViewById(R.id.btnInicio)
        btnMisServicios = findViewById(R.id.btnMisServicios)
        btnHistorial = findViewById(R.id.btnHistorial)
        btnLogout = findViewById(R.id.btnLogout)

        // Configura el listado, la navegación y los filtros de la pantalla.
        recyclerView.layoutManager = LinearLayoutManager(this)
        setupMenu()
        setupFiltros()
        cargarServiciosActivos()
    }

    /**
     * Configura la navegación del menú inferior de esta pantalla.
     */
    private fun setupMenu() {
        btnInicio.setOnClickListener {
            val intent = Intent(this, ClienteMainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnMisServicios.setOnClickListener {
            val intent = Intent(this, MisServiciosActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnHistorial.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogout.setOnClickListener {
            sessionManager.logout()
        }
    }

    /**
     * Configura los filtros rápidos para ver servicios pendientes o en proceso.
     */
    private fun setupFiltros() {
        val toggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroup)

        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btnPendientes -> {
                        filtrarPorEstado("PENDIENTE")
                        // Si quieres cambiar color, hazlo en el XML no aquí
                    }
                    R.id.btnProceso -> {
                        filtrarPorEstado("EN PROCESO")
                        // Si quieres cambiar color, hazlo en el XML no aquí
                    }
                }
            }
        }
    }

    /**
     * Filtra la lista de servicios según el estado seleccionado.
     */
    private fun filtrarPorEstado(estado: String) {
        val listaFiltrada = listaOriginal.filter {
            it.estado?.trim()?.uppercase() == estado
        }

        adapter = VerEstadoAdapter(listaFiltrada)
        recyclerView.adapter = adapter

        if (listaFiltrada.isEmpty()) {
            Toast.makeText(this, "No hay servicios $estado", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Consulta los servicios activos del cliente desde la API y los muestra en la lista.
     */
    private fun cargarServiciosActivos() {
        val idCliente = sessionManager.getUserId()
        val apiService = ApiClient.apiService

        apiService.listarServiciosActivos(idCliente)
            .enqueue(object : Callback<List<ServicioResponse>> {
                override fun onResponse(
                    call: Call<List<ServicioResponse>>,
                    response: Response<List<ServicioResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        listaOriginal = response.body()!!
                        adapter = VerEstadoAdapter(listaOriginal)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(
                            this@VerEstadoActivity,
                            "No hay servicios activos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<ServicioResponse>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@VerEstadoActivity,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
