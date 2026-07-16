package com.servicetech.app.ui.cliente

import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.servicetech.app.R
import com.servicetech.app.data.model.ServicioRequest
import com.servicetech.app.data.model.TipoServicio
import com.servicetech.app.data.remote.ApiClient
import com.servicetech.app.repository.ServicioRepository
import com.servicetech.app.utils.SessionManager
import kotlinx.coroutines.launch
import android.content.Intent

/**
 * Pantalla para crear una nueva orden de servicio desde la app del cliente.
 *
 * Permite seleccionar un tipo de servicio, escribir una descripción y enviarla al backend.
 */
class NuevaOrdenActivity : AppCompatActivity() {

    // Variables principales para la creación de órdenes y manejo de sesión.
    private lateinit var repository: ServicioRepository
    private lateinit var sessionManager: SessionManager

    // Elementos de la interfaz para el formulario y el menú inferior.
    private lateinit var spinnerTipo: Spinner
    private lateinit var edtDescripcion: EditText
    private lateinit var btnCrear: Button
    private lateinit var btnLogout: LinearLayout
    private var listaTipos: List<TipoServicio> = emptyList()

    private lateinit var btnInicio: LinearLayout
    private lateinit var btnMisServicios: LinearLayout
    private lateinit var btnHistorial: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.cliente_nueva_orden_activity)

        // Ajusta el color de la barra de estado para mantener la app coherente visualmente.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.blue_500)
        }

        // Inicializa la sesión y el repositorio para consumir el backend.
        sessionManager = SessionManager(this)
        repository = ServicioRepository(ApiClient.apiService)

        // Vincula los elementos visuales del formulario y del menú inferior.
        spinnerTipo = findViewById(R.id.spinnerTipo)
        edtDescripcion = findViewById(R.id.edtDescripcion)
        btnCrear = findViewById(R.id.btnCrear)
        btnLogout = findViewById(R.id.btnLogout)
        btnInicio = findViewById(R.id.btnInicio)
        btnMisServicios = findViewById(R.id.btnMisServicios)
        btnHistorial = findViewById(R.id.btnHistorial)

        // Configura la carga de datos y las acciones del formulario.
        cargarTiposServicio()
        configurarBotonCrear()
        configurarBotonCerrarSesion()
        configurarBotonInicio()
        configurarBotonMisServicios()
        configurarBotonHistorial()
    }

    /**
     * Carga los tipos de servicio desde la API y los muestra en el Spinner
     */
    /**
     * Carga los tipos de servicio disponibles desde el backend y los muestra en el spinner.
     */
    private fun cargarTiposServicio() {
        lifecycleScope.launch {
            val result = repository.obtenerTiposServicio()

            result.onSuccess { tipos ->
                listaTipos = tipos
                val nombres = tipos.map { it.nombre }

                val adapter = ArrayAdapter(
                    this@NuevaOrdenActivity,
                    android.R.layout.simple_spinner_item,
                    nombres
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerTipo.adapter = adapter
            }

            result.onFailure {
                Toast.makeText(
                    this@NuevaOrdenActivity,
                    "Error cargando tipos de servicio",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Configura el botón para crear una nueva orden de servicio
     * Valida los campos y envía la petición a la API
     */
    /**
     * Valida el formulario y crea una nueva orden de servicio.
     */
    private fun configurarBotonCrear() {
        btnCrear.setOnClickListener {
            // Obtiene y limpia el texto ingresado
            val descripcion = edtDescripcion.text.toString().trim()

            // Validación: campo vacío
            if (descripcion.isEmpty()) {
                edtDescripcion.error = "Ingrese una descripción"
                return@setOnClickListener
            }

            // Validación: lista de tipos vacía
            if (listaTipos.isEmpty()) {
                Toast.makeText(
                    this,
                    "No hay tipos de servicio disponibles",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Obtiene el tipo seleccionado del Spinner
            val tipoSeleccionado = listaTipos[spinnerTipo.selectedItemPosition]

            // Crea el objeto de solicitud con los datos del formulario
            val request = ServicioRequest(
                descripcion = descripcion,
                idCliente = sessionManager.getUserId(),  // Obtiene el ID del usuario logueado
                idTipoServicio = tipoSeleccionado.idTipoServicio
            )

            // Envía la petición a la API
            lifecycleScope.launch {
                val result = repository.crearServicio(request)

                result.onSuccess {
                    Toast.makeText(
                        this@NuevaOrdenActivity,
                        "Servicio creado correctamente",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()  // Cierra la actividad actual
                }

                result.onFailure {
                    Toast.makeText(
                        this@NuevaOrdenActivity,
                        "Error al crear servicio",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    /**
     * Configura el botón de cerrar sesión del menú inferior
     * Limpia la sesión y redirige al login
     */
    /**
     * Cierra la sesión del cliente.
     */
    private fun configurarBotonCerrarSesion() {
        btnLogout.setOnClickListener {
            // Llama al metodo logout del SessionManager que:
            // 1. Limpia los datos de sesión en SharedPreferences
            // 2. Redirige a la pantalla de login
            // 3. Elimina el stack de actividades anteriores
            sessionManager.logout()
        }
    }
    /**
     * Configura el botón de inicio del menú inferior
     * Navega hacia el panel principal del cliente
     */
    /**
     * Navega al panel principal del cliente.
     */
    private fun configurarBotonInicio() {
        btnInicio.setOnClickListener {
            val intent = Intent(this, ClienteMainActivity::class.java)
            startActivity(intent)
            finish()  // Opcional: cierra la actividad actual
        }
    }

    /**
     * Navega a la pantalla de servicios del cliente.
     */
    private fun configurarBotonMisServicios() {
        btnMisServicios.setOnClickListener {
            val intent = Intent(this, MisServiciosActivity::class.java)
            startActivity(intent)
            finish()
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
