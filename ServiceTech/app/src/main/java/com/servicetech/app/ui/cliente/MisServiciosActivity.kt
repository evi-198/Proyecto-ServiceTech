package com.servicetech.app.ui.cliente

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat //importacion para la barra de estado
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioResponse
import com.servicetech.app.data.remote.ApiClient
import com.servicetech.app.ui.cliente.adapter.TablaServicioAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
// importacion para cerrar seccion
import android.widget.LinearLayout
import com.servicetech.app.utils.SessionManager

import android.content.Intent

/**
 * Pantalla de servicios del cliente.
 *
 * Muestra la lista de servicios asociados al usuario y ofrece navegación rápida al
 * inicio, historial y cierre de sesión.
 */
class MisServiciosActivity : AppCompatActivity() {

    // Variables de la interfaz para mostrar los servicios y navegar entre pantallas.
    private lateinit var recyclerServicios: RecyclerView
    private lateinit var sessionManager: SessionManager
    private lateinit var btnLogout: LinearLayout
    private lateinit var btnInicio: LinearLayout
    private lateinit var btnMisServicios: LinearLayout
    private lateinit var btnHistorial: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cliente_mis_servicios_activity)

        // Ajusta el color de la barra de estado para mantener la identidad visual de la app.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.blue_500)
        }

        // Inicializa la sesión y vincula los elementos del menú inferior.
        sessionManager = SessionManager(this)
        btnLogout = findViewById(R.id.btnLogout)
        btnInicio = findViewById(R.id.btnInicio)
        btnMisServicios = findViewById(R.id.btnMisServicios)
        btnHistorial = findViewById(R.id.btnHistorial)

        // Marca la opción actual en el menú inferior para indicar la pantalla activa.
        btnMisServicios.isSelected = true

        // Inicializa la lista de servicios.
        recyclerServicios = findViewById(R.id.recyclerServicios)
        recyclerServicios.layoutManager = LinearLayoutManager(this)

        // Carga los servicios del cliente y configura la navegación del menú.
        cargarServicios()
        configurarBotonCerrarSesion()
        configurarBotonInicio()
        configurarBotonHistorial()
    }


    /**
     * Consulta los servicios del cliente desde la API y los muestra en el RecyclerView.
     */
    private fun cargarServicios() {

        val api = ApiClient.apiService

        val idCliente = sessionManager.getUserId()

        api.listarServicios(idCliente).enqueue(object : Callback<List<ServicioResponse>> {

            override fun onResponse(
                call: Call<List<ServicioResponse>>,
                response: Response<List<ServicioResponse>>
            ) {
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()
                    // [LÍNEA 38] AGREGAR: asignar el adaptador
                    recyclerServicios.adapter = TablaServicioAdapter(lista)
                }
            }

            override fun onFailure(call: Call<List<ServicioResponse>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    // AGREGAR ESTA FUNCIÓN
    /**
     * Cierra la sesión del cliente y vuelve a la pantalla de autenticación.
     */
    private fun configurarBotonCerrarSesion() {
        btnLogout.setOnClickListener {
            sessionManager.logout()
        }
    }

    /**
     * Configura el botón de inicio del menú inferior
     * Navega hacia el panel principal del cliente
     */
    private fun configurarBotonInicio() {
        btnInicio.setOnClickListener {
            val intent = Intent(this, ClienteMainActivity::class.java)
            startActivity(intent)
            finish()  // Opcional: cierra la actividad actual
        }
    }

    /**
     * Navega a la pantalla de historial del cliente.
     */
    private fun configurarBotonHistorial() {
        btnHistorial.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}

