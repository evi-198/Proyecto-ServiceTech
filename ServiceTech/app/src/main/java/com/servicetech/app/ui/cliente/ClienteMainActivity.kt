package com.servicetech.app.ui.cliente

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioResponse
import com.servicetech.app.data.remote.ApiClient
import com.servicetech.app.ui.cliente.adapter.OrdenRecienteAdapter
import com.servicetech.app.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Pantalla principal del cliente.
 * Permite acceder a las principales funcionalidades y visualizar
 * las órdenes recientes registradas por el usuario.
 */
class ClienteMainActivity : AppCompatActivity() {

    // ==========================
    // Variables de sesión
    // ==========================

    private lateinit var sessionManager: SessionManager

    // ==========================
    // Tarjetas principales
    // ==========================

    private lateinit var cardNuevaOrden: CardView
    private lateinit var cardMisServicios: CardView
    private lateinit var cardVerEstado: CardView
    private lateinit var cardHistorial: CardView

    // ==========================
    // Menú inferior
    // ==========================

    private lateinit var btnInicio: LinearLayout
    private lateinit var btnMisServicios: LinearLayout
    private lateinit var btnHistorial: LinearLayout
    private lateinit var btnLogout: LinearLayout

    // ==========================
    // Otros componentes
    // ==========================

    private lateinit var recyclerOrdenesRecientes: RecyclerView
    private lateinit var txtBienvenida: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.cliente_main_activity)

        // ==========================
        // Inicialización de la sesión
        // ==========================

        sessionManager = SessionManager(this)

        // Muestra un saludo personalizado con el nombre del usuario.
        txtBienvenida = findViewById(R.id.txtWelcomeCliente)
        val nombre = sessionManager.getNombre()
        txtBienvenida.text = "Bienvenido, Cliente ${nombre ?: "Usuario"}"

        // ==========================
        // Tarjetas principales
        // ==========================

        cardNuevaOrden = findViewById(R.id.cardNuevaOrden)
        cardMisServicios = findViewById(R.id.cardMisServicios)
        cardVerEstado = findViewById(R.id.cardVerEstado)
        cardHistorial = findViewById(R.id.cardHistorial)

        // ==========================
        // Menú inferior
        // ==========================

        btnInicio = findViewById(R.id.btnInicio)
        btnMisServicios = findViewById(R.id.btnMisServicios)
        btnHistorial = findViewById(R.id.btnHistorial)
        btnLogout = findViewById(R.id.btnLogout)

        // ==========================
        // Otros componentes
        // ==========================

        recyclerOrdenesRecientes = findViewById(R.id.recyclerOrdenesRecientes)
        recyclerOrdenesRecientes.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Marca Inicio como la opción seleccionada.
        btnInicio.isSelected = true

        // Configura la navegación de la pantalla.
        configurarClickListeners()

        // Carga las últimas órdenes registradas.
        cargarOrdenesRecientes()

        // Ajusta los márgenes para las barras del sistema.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    /**
     * Configura la navegación mediante las tarjetas principales
     * y el menú inferior del cliente.
     */
    private fun configurarClickListeners() {

        // ==========================
        // Tarjetas principales
        // ==========================

        // Abre el formulario para registrar una nueva orden.
        cardNuevaOrden.setOnClickListener {
            startActivity(Intent(this, NuevaOrdenActivity::class.java))
        }

        // Muestra todos los servicios registrados por el cliente.
        cardMisServicios.setOnClickListener {
            startActivity(Intent(this, MisServiciosActivity::class.java))
        }

        // Permite consultar el estado de las órdenes activas.
        cardVerEstado.setOnClickListener {
            startActivity(Intent(this, VerEstadoActivity::class.java))
        }

        // Abre el historial de servicios del cliente.
        cardHistorial.setOnClickListener {
            startActivity(Intent(this, HistorialActivity::class.java))
        }

        // ==========================
        // Menú inferior
        // ==========================

        // Pantalla actual.
        btnInicio.setOnClickListener {
            // Sin acción.
        }

        // Acceso rápido a Mis Servicios.
        btnMisServicios.setOnClickListener {
            startActivity(Intent(this, MisServiciosActivity::class.java))
        }

        // Acceso rápido al historial.
        btnHistorial.setOnClickListener {
            startActivity(Intent(this, HistorialActivity::class.java))
        }

        // Cierra la sesión del usuario.
        btnLogout.setOnClickListener {
            sessionManager.logout()
        }
    }

    /**
     * Obtiene las órdenes recientes del cliente
     * y muestra las tres más recientes.
     */
    private fun cargarOrdenesRecientes() {

        val api = ApiClient.apiService
        val idCliente = sessionManager.getUserId()

        api.listarServicios(idCliente)
            .enqueue(object : Callback<List<ServicioResponse>> {

                override fun onResponse(
                    call: Call<List<ServicioResponse>>,
                    response: Response<List<ServicioResponse>>
                ) {

                    if (response.isSuccessful) {

                        val listaCompleta = response.body() ?: emptyList()

                        // Selecciona únicamente las tres órdenes más recientes.
                        val recientes = listaCompleta.take(3)

                        if (recientes.isNotEmpty()) {

                            recyclerOrdenesRecientes.adapter =
                                OrdenRecienteAdapter(recientes)

                        } else {

                            mostrarMensajeSinOrdenes()
                        }
                    }
                }

                override fun onFailure(
                    call: Call<List<ServicioResponse>>,
                    t: Throwable
                ) {

                    t.printStackTrace()
                    mostrarMensajeSinOrdenes()
                }
            })
    }

    /**
     * Oculta la lista cuando el cliente
     * aún no registra órdenes recientes.
     */
    private fun mostrarMensajeSinOrdenes() {

        val tvMensaje = TextView(this)

        tvMensaje.text = "No tienes órdenes recientes"
        tvMensaje.setTextColor(
            ContextCompat.getColor(this, R.color.gray_500)
        )
        tvMensaje.textSize = 14f
        tvMensaje.setPadding(16, 16, 16, 16)

        recyclerOrdenesRecientes.adapter = null
    }
}