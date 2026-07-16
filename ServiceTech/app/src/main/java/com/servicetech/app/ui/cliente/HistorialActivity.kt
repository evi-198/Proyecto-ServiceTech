package com.servicetech.app.ui.cliente

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioResponse
import com.servicetech.app.data.remote.ApiClient
import com.servicetech.app.ui.cliente.adapter.VerEstadoAdapter
import com.servicetech.app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.material.button.MaterialButton

/**
 * Pantalla de historial del cliente.
 *
 * Permite revisar servicios finalizados o cancelados y navegar por el resto del módulo cliente.
 */
class HistorialActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VerEstadoAdapter

    private var listaOriginal: List<ServicioResponse> = emptyList()

    // Menú inferior para navegar entre pantallas del cliente.
    private lateinit var sessionManager: SessionManager
    private lateinit var btnLogout: LinearLayout
    private lateinit var btnInicio: LinearLayout
    private lateinit var btnMisServicios: LinearLayout
    private lateinit var btnHistorial: LinearLayout

    // Botones de filtro para mostrar servicios finalizados o cancelados.
    private lateinit var btnFinalizados: MaterialButton
    private lateinit var btnCancelados: MaterialButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cliente_historial_activity)

        // Ajusta el color de la barra de estado para mantener la interfaz consistente.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.blue_500)
        }

        // Inicializa la sesión y vincula los elementos del menú y filtros.
        sessionManager = SessionManager(this)
        btnLogout = findViewById(R.id.btnLogout)
        btnInicio = findViewById(R.id.btnInicio)
        btnMisServicios = findViewById(R.id.btnMisServicios)
        btnHistorial = findViewById(R.id.btnHistorial)

        // Marca la opción actual como activa en el menú inferior.
        btnHistorial.isSelected = true

        btnFinalizados = findViewById(R.id.btnFinalizados)
        btnCancelados = findViewById(R.id.btnCancelados)

        // Configura la lista de historial y los listeners de navegación.
        recyclerView = findViewById(R.id.recyclerHistorial)
        recyclerView.layoutManager = LinearLayoutManager(this)
        configurarBotonesMenu()
        cargarHistorial()
        configurarFiltros()

    }

    /**
     * Carga el historial de servicios del cliente desde la API.
     */
    private fun cargarHistorial() {

        val idCliente = sessionManager.getUserId()

        Log.d("HISTORIAL", "ID Cliente: $idCliente")

        ApiClient.apiService.obtenerHistorialCliente(idCliente)
            .enqueue(object : Callback<List<ServicioResponse>> {

                override fun onResponse(
                    call: Call<List<ServicioResponse>>,
                    response: Response<List<ServicioResponse>>
                ) {

                    if (response.isSuccessful) {

                        val listaServicios = response.body() ?: emptyList()

                        Log.d("HISTORIAL", "Servicios recibidos: ${listaServicios.size}")

                        if (listaServicios.isEmpty()) {

                            Toast.makeText(
                                this@HistorialActivity,
                                "No hay servicios en el historial",
                                Toast.LENGTH_LONG
                            ).show()

                        } else {

                            listaOriginal = listaServicios

                            adapter = VerEstadoAdapter(listaOriginal)
                            recyclerView.adapter = adapter

                        }
                    }
                }

                override fun onFailure(call: Call<List<ServicioResponse>>, t: Throwable) {

                    Log.e("HISTORIAL", "Error API: ${t.message}")

                    Toast.makeText(
                        this@HistorialActivity,
                        "Error al cargar historial",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    /**
     * Configura todos los botones del menú inferior
     */
    /**
     * Configura la navegación del menú inferior de esta pantalla.
     */
    private fun configurarBotonesMenu() {
        // Botón Cerrar Sesión
        btnLogout.setOnClickListener {
            sessionManager.logout()
        }

        // Botón Inicio
        btnInicio.setOnClickListener {
            val intent = Intent(this, ClienteMainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Botón Mis Servicios
        btnMisServicios.setOnClickListener {
            val intent = Intent(this, MisServiciosActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Botón Historial (actual)
        btnHistorial.setOnClickListener {
            Toast.makeText(this, "Ya estás en Historial", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Configura los filtros para mostrar únicamente servicios finalizados o cancelados.
     */
    private fun configurarFiltros() {

        btnFinalizados.setOnClickListener {

            val listaFiltrada = listaOriginal.filter {
                it.estado.equals("FINALIZADO", ignoreCase = true)
            }

            adapter.updateList(listaFiltrada)

            Toast.makeText(this, "Mostrando finalizados", Toast.LENGTH_SHORT).show()
        }

        btnCancelados.setOnClickListener {

            val listaFiltrada = listaOriginal.filter {
                it.estado.equals("CANCELADO", ignoreCase = true)
            }

            adapter.updateList(listaFiltrada)

            Toast.makeText(this, "Mostrando cancelados", Toast.LENGTH_SHORT).show()
        }
    }

}
